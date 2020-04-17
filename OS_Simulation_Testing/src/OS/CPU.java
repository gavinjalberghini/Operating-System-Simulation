package OS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CPU {

    //TRACKING
    int totalIterAmount = 0;
    int procIterAmount = 0;
    Process[] processesOnCPU = new Process[8];

    //MULTICORE
    Core coreOne = new Core();
    Core coreTwo = new Core();

    //MEMORY
    MMU mmu = new MMU(1024,2048);

    //LEAVING PROCESSES
    ArrayList<Process> leavingProcesses = new ArrayList<>();
    ArrayList<Process> newChildren = new ArrayList<>();
    ArrayList<Integer> cascadingTerminations = new ArrayList<>();

    //MAILBOX
    MailBox mailBox;

    //CHILDREN
    ProcessGenerator childGenerator = new ProcessGenerator();

    public CPU(MailBox mail){
        setMailBox(mail);
    }

    public void cycle(){

        consolidate();
        pushToCores();

        try {
            coreOne.iterate();
            coreTwo.iterate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateFromCores();
    }

    public void evaluateUpdates(Scheduler.MultiLevelFeedbackQueue readyQueueCopy){

        Process[] toBeEvaluated = getProcessesOnCPU();

        for(int i=0;i<8;i++){
            if(toBeEvaluated[i]!=null){
                if(toBeEvaluated[i].getPcb().getProcessState() == PCB.ProcessState.EXIT ||
                  ((toBeEvaluated[i].getPcb().getProgramCounter() == toBeEvaluated[i].getPcb().getInstructions().size()-1) &&
                  toBeEvaluated[i].getPcb().getInstructions().get(toBeEvaluated[i].getPcb().getProgramCounter()).equalsIgnoreCase("FINISHED."))){ //LEAVING OS

                    resourceRelief(toBeEvaluated[i]);
                    updateProcInfoAtIndex(null, i);

                } else if (toBeEvaluated[i].getPcb().getProcessState() == PCB.ProcessState.WAIT){ //SWAP TO WAITING QUEUE
                    if (toBeEvaluated[i].getPcb().isPageLoadNeeded())
                        toBeEvaluated[i] = pageSwap(toBeEvaluated[i]);
                    addToLeaveQueue(toBeEvaluated[i]);
                    updateProcInfoAtIndex(null, i);

                } else if (toBeEvaluated[i].getPcb().getProcessState() == PCB.ProcessState.READY){ //YIELD TO READY QUEUE
                    if (toBeEvaluated[i].getPcb().isPageLoadNeeded())
                        toBeEvaluated[i] = pageSwap(toBeEvaluated[i]);
                    toBeEvaluated[i].getPcb().shiftToNextInstruction();
                    addToLeaveQueue(toBeEvaluated[i]);
                    updateProcInfoAtIndex(null, i);

                } else if (toBeEvaluated[i].getPcb().getProcessState() == PCB.ProcessState.RUN){ //STAYING ON CPU

                    String nextInstructionPath = toBeEvaluated[i].getPcb().getInstructions().get(toBeEvaluated[i].getPcb().getProgramCounter());
                    nextInstructionPath = nextInstructionPath.substring(0, nextInstructionPath.indexOf("."));

                    if(nextInstructionPath.equalsIgnoreCase("SPAWN")){
                        toBeEvaluated[i] = spawnChild(toBeEvaluated[i]);
                        toBeEvaluated[i].getPcb().shiftToNextInstruction();

                    } else if (nextInstructionPath.equalsIgnoreCase("PROC-COMM")){
                        toBeEvaluated[i] = launchIPC(toBeEvaluated[i], readyQueueCopy);
                        toBeEvaluated[i].getPcb().shiftToNextInstruction();
                    }

                    if (toBeEvaluated[i].getPcb().isPageLoadNeeded())
                        toBeEvaluated[i] = pageSwap(toBeEvaluated[i]);

                    updateProcInfoAtIndex(toBeEvaluated[i], i);

                }

            } else {
                updateProcInfoAtIndex(null, i);
            }
        }

    }

    public void resourceRelief(Process p){

        for(int frameAddr : p.getPageTable().values()){
            if(frameAddr > getMmu().getRam().getSize())
                getMmu().getVirtualMemory().deallocateFrame(frameAddr);
            else
                getMmu().getRam().deallocateFrame(frameAddr);
        }

        getMmu().setResourceOne(getMmu().getResourceOne() + p.getPcb().getResourceOneReq());
        getMmu().setResourceTwo(getMmu().getResourceTwo() + p.getPcb().getResourceTwoReq());

        for(int childPid:p.getPdt().getChildrenPids()){
            getCascadingTerminations().add(childPid);
        }

    }

    public Process launchIPC(Process p, Scheduler.MultiLevelFeedbackQueue readyQueueCopy){

        for(Process.Pipe pipe : p.getPipes()){
            Process companion = readyQueueCopy.getProcessCopyFromReadyQueue(pipe.connectedProcessPid);
            if(companion != null){
                int returnIndex = p.getPipes().indexOf(pipe);
                pipe.communicate(companion);
                p.getPipes().set(returnIndex, pipe);
            }
        }

        String mailBoxMessage;

        for(MailBox.TriTuple mail : getMailBox().getMail()){
            mailBoxMessage = mail.getMessage(p.getPcb().getMailKey());
            if(mailBoxMessage != null){
                p.getPcb().setMailData(mailBoxMessage);
                String postData = " XXXXX - Posted By " + p.getPcb().getPid();
                mail.setData(postData);
            }
        }

        return p;

    }

    public Process spawnChild(Process p){
        Random generator = new Random();
        Process child = null;

        switch (p.getPcb().getJobType()) {
            case "Media Player":
                child = getChildGenerator().buildMediaPlayerProcess();
                break;
            case "Photo Editor":
                child = getChildGenerator().buildPhotoEditorProcess();
                break;
            case "Virus Scanner":
                child = getChildGenerator().buildVirusScannerProcess();
                break;
            case "Web Browser":
                child = getChildGenerator().buildWebBrowserProcess();
                break;
            case "Word Processor":
                child = getChildGenerator().buildWordProcessorProcess();
                break;
        }

        child.getPdt().setIsChild(true);
        child.getPdt().setParentPid(p.getPcb().getPid());

        int childLock = generator.nextInt();
        int childKey = generator.nextInt();
        int parentLock = childKey;
        int parentKey = childLock;

        child.buildPipe(p.getPcb().getPid(), childLock, childKey);
        p.buildPipe(child.getPcb().getPid(), parentLock, parentKey);

        p.getPdt().setIsParent(true);
        p.getPdt().addChildToPDT(child.getPcb().getPid());

        getNewChildren().add(child);

        return p;

    }

    public Process pageSwap(Process p){

        if(p.getPcb().isInRamOnly() == false) {
            HashMap<Integer, Integer> pageTable = p.getPageTable();
            HashMap<Integer, Integer> validBits = p.getValidBits();

            int stopper;

            if (p.getPcb().getNextLoadAmnt() + p.getPcb().getOffset() <= p.getPcb().getMemoryReq())
                stopper = p.getPcb().getNextLoadAmnt() + p.getPcb().getOffset();
            else
                stopper = p.getPcb().getMemoryReq();

            for (int i = p.getPcb().getOffset(); i < stopper; i++) {
                int swapIn = p.getPcb().getBaseRegister() + i;
                int swapOut = p.getPcb().getBaseRegister() + i - p.getPcb().getNextLoadAmnt();
                int swapOutAddr = pageTable.get(swapOut);
                int swapInAddr = pageTable.get(swapIn);
                Frame rFrame = getMmu().getRam().getFrames().get(swapOutAddr);
                Frame vmFrame = getMmu().getVirtualMemory().getFrames().get(swapInAddr);
                pageTable.put(swapIn, rFrame.getPhysicalAddress());
                validBits.put(swapIn, 0);
                pageTable.put(swapOut, vmFrame.getPhysicalAddress());
                validBits.put(swapOut, 1);
                getMmu().updateMemoryMap(swapIn, rFrame.getPhysicalAddress());
                getMmu().updateMemoryMap(swapOut, vmFrame.getPhysicalAddress());
            }
        }

        p.getPcb().setOffset(p.getPcb().getOffset() + p.getPcb().getNextLoadAmnt());
        p.getPcb().setPageLoadNeeded(false);
        return p;

    }

    public void pushToCores(){
        coreOne.updateProcInfoAtIndex(getProcessesOnCPU()[0],0);
        coreOne.updateProcInfoAtIndex(getProcessesOnCPU()[1],1);
        coreOne.updateProcInfoAtIndex(getProcessesOnCPU()[2],2);
        coreOne.updateProcInfoAtIndex(getProcessesOnCPU()[3],3);
        coreTwo.updateProcInfoAtIndex(getProcessesOnCPU()[4],0);
        coreTwo.updateProcInfoAtIndex(getProcessesOnCPU()[5],1);
        coreTwo.updateProcInfoAtIndex(getProcessesOnCPU()[6],2);
        coreTwo.updateProcInfoAtIndex(getProcessesOnCPU()[7],3);
    }

    public void updateFromCores(){

        for(int i=0; i<4; i++)
            updateProcInfoAtIndex(this.coreOne.getHardwareThreads()[i],i);

        for(int i=0; i<4; i++)
            updateProcInfoAtIndex(this.coreTwo.getHardwareThreads()[i],i+4);

    }

    public void consolidate(){

        Process[] toConsolidate = getProcessesOnCPU();
        Process[] consolidated = {null,null,null,null,null,null,null,null};

        int addIndex = 0;
        for(int i=0; i<8; i++){
            if(toConsolidate[i]!=null){
                consolidated[addIndex] = toConsolidate[i];
                addIndex++;
            }
        }

        setProcessesOnCPU(consolidated);

    }

    public boolean addProcessToCpu(Process p){
        for(int i=0; i<8; i++){
            if(getProcessesOnCPU()[i]==null) {
                updateProcInfoAtIndex(p, i);
                return true;
            }
        }

        return false;
    }

    public void moveProcessToLeaveQueue(int index){
        Process outbound = getProcessesOnCPU()[index];
        updateProcInfoAtIndex(null, index);
        addToLeaveQueue(outbound);
    }

    public void updateProcInfoAtIndex(Process p, int index){

        Process[] updatedProcesses = getProcessesOnCPU();
        updatedProcesses[index] = p;
        setProcessesOnCPU(updatedProcesses);

    }

    public void addToLeaveQueue(Process p){
        this.leavingProcesses.add(p);
    }

    public Process getFromLeaveQueue(){
        return this.leavingProcesses.remove(0);
    }

    public ArrayList<Process> getNewChildren() {
        return newChildren;
    }

    public void setNewChildren(ArrayList<Process> newChildren) {
        this.newChildren = newChildren;
    }

    public void addToNewChildren(Process p){
        this.newChildren.add(p);
    }

    public Process getFromNewChildren(){
        return this.newChildren.remove(0);
    }

    public Process[] getProcessesOnCPU() {
        return processesOnCPU;
    }

    public void setProcessesOnCPU(Process[] processesOnCPU) {
        this.processesOnCPU = processesOnCPU;
    }

    public ProcessGenerator getChildGenerator() {
        return childGenerator;
    }

    public ArrayList<Process> getLeavingProcesses() {
        return leavingProcesses;
    }

    public void setLeavingProcesses(ArrayList<Process> leavingProcesses) {
        this.leavingProcesses = leavingProcesses;
    }

    public ArrayList<Integer> getCascadingTerminations() {
        return cascadingTerminations;
    }

    public void setCascadingTerminations(ArrayList<Integer> cascadingTerminations) {
        this.cascadingTerminations = cascadingTerminations;
    }

    public MailBox getMailBox() {
        return mailBox;
    }

    public void setMailBox(MailBox mailBox) {
        this.mailBox = mailBox;
    }

    public Core getCoreOne() {
        return coreOne;
    }

    public Core getCoreTwo() {
        return coreTwo;
    }

    public void setProcIterAmount(int procIterAmount) {
        this.procIterAmount = procIterAmount;
    }

    public int getProcIterAmount() {
        return procIterAmount;
    }

    public void setTotalIterAmount(int totalIterAmount) {
        this.totalIterAmount = totalIterAmount;
    }

    public int getTotalIterAmount() {
        return totalIterAmount;
    }

    public void setMmu(MMU mmu) {
        this.mmu = mmu;
    }

    public MMU getMmu() {
        return mmu;
    }

    public String toString(){
        String result = "";

        result += "CPU THREADS\n";

        if(getProcessesOnCPU()[0] != null)
            if(getProcessesOnCPU()[0].getPcb().getProgramCounter() == getProcessesOnCPU()[0].getPcb().getInstructions().size())
                result += "Thread 1 - " + getProcessesOnCPU()[0].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[0].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[0].getPcb().getInstructions().size() + " : EXIT\n";
            else
                result += "Thread 1 - " + getProcessesOnCPU()[0].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[0].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[0].getPcb().getInstructions().size() + " : " + getProcessesOnCPU()[0].getPcb().getInstructions().get(getProcessesOnCPU()[0].getPcb().getProgramCounter()) + "\n";
        else
            result += "Thread 1 - Empty\n";

        if(getProcessesOnCPU()[1] != null)
            if(getProcessesOnCPU()[1].getPcb().getProgramCounter() == getProcessesOnCPU()[1].getPcb().getInstructions().size())
                result += "Thread 2 - " + getProcessesOnCPU()[1].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[1].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[1].getPcb().getInstructions().size() + " : EXIT\n";
            else
                result += "Thread 2 - " + getProcessesOnCPU()[1].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[1].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[1].getPcb().getInstructions().size() + " : " + getProcessesOnCPU()[1].getPcb().getInstructions().get(getProcessesOnCPU()[1].getPcb().getProgramCounter()) + "\n";
        else
            result += "Thread 2 - Empty\n";

        if(getProcessesOnCPU()[2] != null)
            if(getProcessesOnCPU()[2].getPcb().getProgramCounter() == getProcessesOnCPU()[2].getPcb().getInstructions().size())
                result += "Thread 3 - " + getProcessesOnCPU()[2].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[2].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[2].getPcb().getInstructions().size() + " : EXIT\n";
            else
                result += "Thread 3 - " + getProcessesOnCPU()[2].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[2].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[2].getPcb().getInstructions().size() + " : " + getProcessesOnCPU()[2].getPcb().getInstructions().get(getProcessesOnCPU()[2].getPcb().getProgramCounter()) + "\n";
        else
            result += "Thread 3 - Empty\n";

        if(getProcessesOnCPU()[3] != null)
            if(getProcessesOnCPU()[3].getPcb().getProgramCounter() == getProcessesOnCPU()[3].getPcb().getInstructions().size())
                result += "Thread 4 - " + getProcessesOnCPU()[3].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[3].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[3].getPcb().getInstructions().size() + " : EXIT\n";
            else
                result += "Thread 4 - " + getProcessesOnCPU()[3].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[3].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[3].getPcb().getInstructions().size() + " : " + getProcessesOnCPU()[3].getPcb().getInstructions().get(getProcessesOnCPU()[3].getPcb().getProgramCounter()) + "\n";
        else
            result += "Thread 4 - Empty\n";

        if(getProcessesOnCPU()[4] != null)
            if(getProcessesOnCPU()[4].getPcb().getProgramCounter() == getProcessesOnCPU()[4].getPcb().getInstructions().size())
                result += "Thread 5 - " + getProcessesOnCPU()[4].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[4].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[4].getPcb().getInstructions().size() + " : EXIT\n";
            else
                result += "Thread 5 - " + getProcessesOnCPU()[4].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[4].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[4].getPcb().getInstructions().size() + " : " + getProcessesOnCPU()[4].getPcb().getInstructions().get(getProcessesOnCPU()[4].getPcb().getProgramCounter()) + "\n";
        else
            result += "Thread 5 - Empty\n";

        if(getProcessesOnCPU()[5] != null)
            if(getProcessesOnCPU()[5].getPcb().getProgramCounter() == getProcessesOnCPU()[5].getPcb().getInstructions().size())
                result += "Thread 6 - " + getProcessesOnCPU()[5].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[5].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[5].getPcb().getInstructions().size() + " : EXIT\n";
            else
                result += "Thread 6 - " + getProcessesOnCPU()[5].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[5].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[5].getPcb().getInstructions().size() + " : " + getProcessesOnCPU()[5].getPcb().getInstructions().get(getProcessesOnCPU()[5].getPcb().getProgramCounter()) + "\n";
        else
            result += "Thread 6 - Empty\n";

        if(getProcessesOnCPU()[6] != null)
            if(getProcessesOnCPU()[6].getPcb().getProgramCounter() == getProcessesOnCPU()[6].getPcb().getInstructions().size())
                result += "Thread 7 - " + getProcessesOnCPU()[6].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[6].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[6].getPcb().getInstructions().size() + " : EXIT\n";
            else
                result += "Thread 7 - " + getProcessesOnCPU()[6].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[6].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[6].getPcb().getInstructions().size() + " : " + getProcessesOnCPU()[6].getPcb().getInstructions().get(getProcessesOnCPU()[6].getPcb().getProgramCounter()) + "\n";
        else
            result += "Thread 7 - Empty\n";

        if(getProcessesOnCPU()[7] != null)
            if(getProcessesOnCPU()[7].getPcb().getProgramCounter() == getProcessesOnCPU()[7].getPcb().getInstructions().size())
                result += "Thread 8 - " + getProcessesOnCPU()[7].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[7].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[7].getPcb().getInstructions().size() + " : EXIT\n";
            else
            result += "Thread 8 - " + getProcessesOnCPU()[7].getPcb().getPid() + " : On instruction " + getProcessesOnCPU()[7].getPcb().getProgramCounter() + " / " + getProcessesOnCPU()[7].getPcb().getInstructions().size() + " : " + getProcessesOnCPU()[7].getPcb().getInstructions().get(getProcessesOnCPU()[7].getPcb().getProgramCounter()) + "\n";
        else
            result += "Thread 8 - Empty\n";

        return result;
    }

}
