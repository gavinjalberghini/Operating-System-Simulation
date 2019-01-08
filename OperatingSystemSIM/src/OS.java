import java.util.ArrayList;
import java.util.Random;

public class OS {

    ProcessGenerator pGenerator = new ProcessGenerator();
    Debugger debugger = new Debugger();
    MailBox mailBox = new MailBox();
    CPU cpu = new CPU(mailBox);
    Scheduler scheduler;
    Random rand = new Random();
    Process lookUp = new Process();
    ArrayList<Integer> toTerminate = new ArrayList<>();

    public OS(boolean sort){
        System.out.println("System booting...");
        System.out.println("System ready for commands...");
        setScheduler(new Scheduler(sort));
    }

    public void launchCommand(String s){

        System.out.println("Running...");

        switch(s.substring(0,s.indexOf('('))){
            case"Rand":
                ArrayList<Process> newProcesses = pGenerator.generateProcesses(Integer.parseInt(s.substring(s.indexOf('(')+1, s.indexOf(')'))));
                acceptProcesses(newProcesses);
                break;
            case"Run":
                tryAllocatingMemToNewProcesses();
                fillCPU();
                getCpu().cycle();
                getCpu().evaluateUpdates(getScheduler().getReadyQueue());
                getNewChildrenOffCPU();
                getNowLeavingOffCPU();
                ArrayList<Integer> toBeKilled = getTerminationPIDsOffCPU();
                prepCascadingTermination(toBeKilled);
                setTerminationStatuses();
                getScheduler().iterate();
                break;
            case"LookUp":
                int pid = Integer.parseInt(s.substring(s.indexOf('(')+1, s.indexOf(')')));

                if(getScheduler().getReadyQueue().contains(pid)){
                    setLookUp(getScheduler().getReadyQueue().getProcessCopyFromReadyQueue(pid));
                } else if (getScheduler().containsInIo(pid)){
                    setLookUp(getScheduler().getCopyFromIoQueue(pid));
                } else if (getScheduler().containsInMemoryWait(pid)){
                    setLookUp(getScheduler().getCoptFromMemoryWait(pid));
                } else {
                    for(int i=0; i<8; i++)
                        if(getCpu().getProcessesOnCPU()[i] != null)
                            if(getCpu().getProcessesOnCPU()[i].getPcb().getPid() == pid)
                                setLookUp(getCpu().getProcessesOnCPU()[i]);
                }

                break;
        }
    }

    public void fillCPU(){
        boolean inLoop = true;
        while(inLoop){

            Process toAdd = getScheduler().getReadyQueue().pop();

            if(toAdd != null && getCpu().addProcessToCpu(toAdd)==true){

            } else {
                if(toAdd != null)
                    getScheduler().getReadyQueue().assignProcessToMultiQueue(toAdd);
                inLoop = false;
            }

        }
    }

    public ArrayList<Integer> getTerminationPIDsOffCPU(){
        ArrayList<Integer> toBeKilled = new ArrayList<>();

        for(int i=getCpu().getCascadingTerminations().size()-1; i>=0; i--){
            toBeKilled.add(getCpu().getCascadingTerminations().remove(i));
        }

        return toBeKilled;
    }

    public void prepCascadingTermination(ArrayList<Integer> toBeKilled){

        Process p;
        for(Integer pid : toBeKilled){
            getTerminationPIDsOffCPU().add(pid);
        }

        for(Integer pid : toBeKilled){
            if(getScheduler().getReadyQueue().contains(pid)){
                p=getScheduler().getReadyQueue().getProcessCopyFromReadyQueue(pid);
                if(p.getPdt().getChildrenPids().size()>0){
                    prepCascadingTermination(p.getPdt().getChildrenPids());
                }
            }
        }
    }

    public void setTerminationStatuses(){
        Process p;

        for(Integer pid : getToTerminate()){

            if(getScheduler().getReadyQueue().getBackgroundQueue().get(pid) != null){
                p=getScheduler().getReadyQueue().getBackgroundQueue().get(pid);
                getScheduler().getReadyQueue().removeFromBackgroundQueue(pid);
                p.getPcb().setProgramCounter(p.getPcb().getInstructions().size());
                p.getPcb().getInstructions().set(p.getPcb().getProgramCounter()-1, "FINISHED.");
                getScheduler().getReadyQueue().addToBackgroundQueue(p);
            } else if(getScheduler().getReadyQueue().getInterQueue().get(pid) != null){
                p=getScheduler().getReadyQueue().getInterQueue().get(pid);
                getScheduler().getReadyQueue().removeFromInterQueue(pid);
                p.getPcb().setProgramCounter(p.getPcb().getInstructions().size());
                p.getPcb().getInstructions().set(p.getPcb().getProgramCounter()-1, "FINISHED.");
                getScheduler().getReadyQueue().addToInterQueue(p);
            } else if(getScheduler().getReadyQueue().getForegroundQueue().get(pid) != null){
                p=getScheduler().getReadyQueue().getForegroundQueue().get(pid);
                getScheduler().getReadyQueue().removeFromForegroundQueue(pid);
                p.getPcb().setProgramCounter(p.getPcb().getInstructions().size());
                p.getPcb().getInstructions().set(p.getPcb().getProgramCounter()-1, "FINISHED.");
                getScheduler().getReadyQueue().addToForegroundQueue(p);
            } else if(getScheduler().containsInMemoryWait(pid)){
                p=getScheduler().getCoptFromMemoryWait(pid);
                getScheduler().getWaitingForMemoryQueue().remove(p);
            } else if(getScheduler().containsInIo(pid)){
                p=getScheduler().getCopyFromIoQueue(pid);
                p.getPcb().setProgramCounter(p.getPcb().getInstructions().size());
                p.getPcb().getInstructions().set(p.getPcb().getProgramCounter()-1, "FINISHED.");
                getScheduler().getReadyQueue().addToForegroundQueue(p);
            } else {

                for(int i=0; i<8; i++){
                    if(getCpu().getProcessesOnCPU()[i] != null){
                        if(getCpu().getProcessesOnCPU()[i].getPcb().getPid() == pid){
                            getCpu().getProcessesOnCPU()[i].getPcb().setProgramCounter(getCpu().getProcessesOnCPU()[i].getPcb().getInstructions().size());
                            getCpu().getProcessesOnCPU()[i].getPcb().getInstructions().set(getCpu().getProcessesOnCPU()[i].getPcb().getProgramCounter()-1, "FINISHED.");
                        }
                    }
                }

            }

        }

        setToTerminate(new ArrayList<Integer>());

    }

    public void getNewChildrenOffCPU(){
        for(int i=getCpu().getNewChildren().size()-1; i>=0; i--){
            getScheduler().addToWaitingForMemoryQueue(getCpu().getFromNewChildren());
        }
    }

    public void getNowLeavingOffCPU(){
        for(int i=getCpu().getLeavingProcesses().size()-1; i>=0; i--){
            Process shiftingProcess = getCpu().getFromLeaveQueue();

            if(shiftingProcess.getPcb().getProcessState() == PCB.ProcessState.READY)
                getScheduler().getReadyQueue().assignProcessToMultiQueue(shiftingProcess);
            else if(shiftingProcess.getPcb().getProcessState() == PCB.ProcessState.WAIT){
                getScheduler().getWaitingForIoQueue().add(shiftingProcess);
            }
        }
    }

    public ArrayList<Process> processResourceAllocation(ArrayList<Process> toBeAssigned) {

        ArrayList<Process> result = new ArrayList<>();

        for(Process p:toBeAssigned){
            if(getCpu().getMmu().getResourceOne() >= p.getPcb().getResourceOneReq() && getCpu().getMmu().getResourceTwo() >= p.getPcb().getResourceTwoReq() && p.getPcb().hasProcessResources()==false){
                getCpu().getMmu().setResourceOne(getCpu().getMmu().getResourceOne()-p.getPcb().getResourceOneReq());
                getCpu().getMmu().setResourceTwo(getCpu().getMmu().getResourceTwo()-p.getPcb().getResourceTwoReq());
                p.getPcb().setHasProcessResources(true);
            }

            result.add(p);
        }

        return result;

    }

    public Process deadlockAvoidance(Process p){

        if(p.getPcb().hasProcessResources == true) {
            getCpu().getMmu().setResourceOne(getCpu().getMmu().getResourceOne() + p.getPcb().getResourceOneReq());
            getCpu().getMmu().setResourceTwo(getCpu().getMmu().getResourceTwo() + p.getPcb().getResourceTwoReq());
            p.getPcb().setHasProcessResources(false);
        }

        return p;
    }

    public ArrayList<Process> memoryAllocation(ArrayList<Process> toBeAssigned){
        ArrayList<Process> result = new ArrayList<>();

        for(Process p : toBeAssigned){

            if(p.getPcb().hasProcessResources() == true){

                int primarySize = p.getPcb().getNextLoadAmnt();
                int secondarySize = p.getPcb().getMemoryReq() - primarySize;

                if(getCpu().getMmu().getRam().getNumOfFreeFrames() >= primarySize && getCpu().getMmu().getVirtualMemory().getNumOfFreeFrames() >= secondarySize){
                    for (int i = 0; i < primarySize; i++)
                        p.addToPageTable(p.getPcb().getPageCtr(), getCpu().getMmu().getRam().allocateFrame(p.getPcb().getPid()).getPhysicalAddress(), 0);
                    for (int i = 0; i < secondarySize; i++)
                        p.addToPageTable(p.getPcb().getPageCtr(), getCpu().getMmu().getVirtualMemory().allocateFrame(p.getPcb().getPid()).getPhysicalAddress(), 1);

                    p.getPcb().setHasMemory(true);

                    p.getPcb().setProcessState(PCB.ProcessState.READY);
                    result.add(p);
                } else if (getCpu().getMmu().getRam().getNumOfFreeFrames() >= (primarySize+secondarySize)) {
                    for (int i = 0; i < primarySize + secondarySize; i++)
                        p.addToPageTable(p.getPcb().getPageCtr(), getCpu().getMmu().getRam().allocateFrame(p.getPcb().getPid()).getPhysicalAddress(), 0);

                    p.getPcb().setInRamOnly(true);
                    p.getPcb().setHasMemory(true);
                    p.getPcb().setProcessState(PCB.ProcessState.READY);
                    result.add(p);
                } else {
                    p.getPcb().setProcessState(PCB.ProcessState.NEW);
                    p = deadlockAvoidance(p);
                    result.add(p);
                }



            } else {
                p.getPcb().setProcessState(PCB.ProcessState.NEW);
                result.add(p);
            }

        }

        return result;

    }

    public void tryAllocatingMemToNewProcesses(){

        ArrayList<Process> toBeAllocated = new ArrayList<>();

        for(int i=getScheduler().getWaitingForMemoryQueue().size()-1; i>=0; i--)
            toBeAllocated.add(getScheduler().getWaitingForMemoryQueue().remove(i));

        toBeAllocated = processResourceAllocation(toBeAllocated);
        toBeAllocated = memoryAllocation(toBeAllocated);
        assignToQueuesPostMemAssignmentAttempt(toBeAllocated);

    }

    public void assignToQueuesPostMemAssignmentAttempt(ArrayList<Process> toBeEvaluated){

        for(Process p : toBeEvaluated){

            if(p.getPcb().hasMemory() && p.getPcb().hasProcessResources()){
                p.getPcb().setProcessState(PCB.ProcessState.READY);
                getScheduler().getReadyQueue().assignProcessToMultiQueue(p);
            } else {
                p.getPcb().setProcessState(PCB.ProcessState.NEW);
                getScheduler().getWaitingForMemoryQueue().add(p);
            }

        }

    }

    public void moveToWaitForMemQueue(ArrayList<Process> newProcesses){
        for(Process p : newProcesses) {
            getScheduler().getWaitingForMemoryQueue().add(p);
        }
    }

    public void acceptProcesses(ArrayList<Process> newProcesses){

        newProcesses = assignMailBox(newProcesses);
        moveToWaitForMemQueue(newProcesses);

    }

    public ArrayList<Process> assignMailBox(ArrayList<Process> newProcesses){

        for(Process p : newProcesses) {
            for (MailBox.TriTuple mail : getMailBox().getMail()) {
                if (mail.type.equalsIgnoreCase(p.getPcb().getJobType())) {
                    p.getPcb().setMailKey(mail.lock);
                }
            }
        }

        return newProcesses;

    }

    public ArrayList<Integer> getToTerminate() {
        return toTerminate;
    }

    public void setToTerminate(ArrayList<Integer> toTerminate) {
        this.toTerminate = toTerminate;
    }

    public void setMailBox(MailBox mailBox) {
        this.mailBox = mailBox;
    }

    public MailBox getMailBox() {
        return mailBox;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Debugger getDebugger() {
        return debugger;
    }

    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }

    public void setLookUp(Process lookUp) {
        this.lookUp = lookUp;
    }

    public Process getLookUp() {
        return lookUp;
    }
}
