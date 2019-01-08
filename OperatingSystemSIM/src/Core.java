import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Core {

    //TRACKING
    Process[] hardwareThreads = new Process[4];

    //STATUS
    boolean isOpenThread = true;
    boolean threadOneOpen = true;
    boolean threadTwoOpen = true;
    boolean threadThreeOpen = true;
    boolean threadFourOpen = true;

    public void iterate() throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<Process>> threadResults = new ArrayList<>();

        //Makes sure only making threads for process that exist
        for(int i=0; i<getNumOfOccupiedCoreSlots(); i++) {
            Callable thread = new HardwareThread(getHardwareThreads()[i]);
            Future<Process> future = executor.submit(thread);
            threadResults.add(future);
        }

        for(Future<Process> fut : threadResults){
            try {

                Process output = fut.get();
                int index = getIndexOfProcessOnCore(output.getPcb().getPid());

                if(index!=-1){
                    updateProcInfoAtIndex(output,index);
                } else {
                    System.out.println("ERROR HAS OCCURRED IN CORE");
                }

            }catch(Exception e){e.printStackTrace();}
        }

        executor.shutdown();
    }

    public int getNumOfOccupiedCoreSlots(){
        int count = 0;
        for(int i=0; i<4; i++)
            if(getHardwareThreads()[i]!=null)
                count++;
            return count;
    }

    public void updateProcInfoAtIndex(Process p, int index){

        Process[] updatedProcesses = getHardwareThreads();
        updatedProcesses[index] = p;
        setHardwareThreads(updatedProcesses);

    }

    public void updateCoreAvailability(){
        updateThreadStatuses();

        if(isOpenThread() || isThreadTwoOpen() || isThreadThreeOpen() || isThreadFourOpen()){
            setOpenThread(true);
        } else {
            setOpenThread(false);
        }
    }

    public int getIndexOfProcessOnCore(int pid){

        for(int i=0; i<4; i++)
            if(getHardwareThreads()[i].getPcb().getPid() == pid)
                return i;

        return -1;
    }

    public void updateThreadStatuses(){
        if(this.hardwareThreads[0]==null)
            setThreadOneOpen(true);
        else
            setThreadOneOpen(false);

        if(this.hardwareThreads[1]==null)
            setThreadTwoOpen(true);
        else
            setThreadTwoOpen(false);

        if(this.hardwareThreads[2]==null)
            setThreadThreeOpen(true);
        else
            setThreadThreeOpen(false);

        if(this.hardwareThreads[3]==null)
            setThreadFourOpen(true);
        else
            setThreadFourOpen(false);
    }

    public boolean isThreadOneOpen() {
        return threadOneOpen;
    }

    public boolean isThreadTwoOpen() {
        return threadTwoOpen;
    }

    public boolean isThreadThreeOpen() {
        return threadThreeOpen;
    }

    public boolean isThreadFourOpen() {
        return threadFourOpen;
    }

    public void setThreadOneOpen(boolean threadOneOpen) {
        this.threadOneOpen = threadOneOpen;
    }

    public void setThreadTwoOpen(boolean threadTwoOpen) {
        this.threadTwoOpen = threadTwoOpen;
    }

    public void setThreadThreeOpen(boolean threadThreeOpen) {
        this.threadThreeOpen = threadThreeOpen;
    }

    public void setThreadFourOpen(boolean threadFourOpen) {
        this.threadFourOpen = threadFourOpen;
    }

    public boolean isOpenThread() {
        return isOpenThread;
    }

    public void setOpenThread(boolean openThread) {
        isOpenThread = openThread;
    }

    public Process[] getHardwareThreads() {
        return hardwareThreads;
    }

    public void setHardwareThreads(Process[] hardwareThreads) {
        this.hardwareThreads = hardwareThreads;
    }

    public static class HardwareThread implements Callable<Process> {

        //METADATA
        int iterCount;

        //CRITICAL SECTION
        static Semaphore mutex = new Semaphore(1);

        //PROCESS BEING EDITED
        private Process activeProcess;

        public HardwareThread(Process toActivate){
            this.activeProcess=toActivate;
            this.iterCount=toActivate.getPcb().getLastCpuIter();
        }

        public Process call() throws Exception{
            getActiveProcess().getPcb().setProcessState(PCB.ProcessState.RUN);

            while(coreIteration() == false){}

            return getActiveProcess();

        }

        private boolean coreIteration(){

            Process editing = getActiveProcess();
            boolean isFinished = false;

            if(editing.getPcb().getProgramCounter() == editing.getPcb().getInstructions().size()){
                editing.getPcb().setProcessState(PCB.ProcessState.EXIT);
                isFinished = true;
            } else {

                int instructionNum = editing.getPcb().getProgramCounter();
                String instruction = editing.getPcb().getInstructions().get(instructionNum);

                switch (instruction.substring(0, instruction.indexOf("."))) {
                    case "CALCULATE":
                        isFinished = runCalculate();
                        break;
                    case "YIELD":
                        isFinished = yield();
                        break;
                    case "SPAWN":
                        isFinished = spawnChild();
                        break;
                    case "I/O":
                        isFinished = swapToIoWait();
                        break;
                    case "PROC-COMM":
                        isFinished = startProcComm();
                        break;
                    case "CRITICAL":
                        try {
                            isFinished = launchCriticalSection();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }

                if (isFinished == false)
                    isFinished = checkForPageLoad();
                else
                    checkForPageLoad();

            }

            return isFinished;
        }

        public boolean runCalculate(){
            boolean kickout = false;

            int prgCntr = getActiveProcess().getPcb().getProgramCounter();
            ArrayList<String> prgText = getActiveProcess().getPcb().getInstructions();
            String instruction = prgText.get(prgCntr);
            int remainingCycles = Integer.parseInt(instruction.substring(instruction.indexOf(".")+1,instruction.lastIndexOf(".")));

            if(remainingCycles > 1){
                remainingCycles -= 1;
                instruction = "CALCULATE." + remainingCycles + ".";
                prgText.set(prgCntr, instruction);
                getActiveProcess().getPcb().setInstructions(prgText);
            } else if(remainingCycles == 1){
                instruction = "FINISHED.";
                prgText.set(prgCntr, instruction);
                prgCntr ++;
                getActiveProcess().getPcb().setInstructions(prgText);
                getActiveProcess().getPcb().setProgramCounter(prgCntr);

                if(prgCntr == prgText.size()) {
                    kickout = true;
                    getActiveProcess().getPcb().setProcessState(PCB.ProcessState.EXIT);
                }
            }
            return kickout;
        }

        public boolean yield(){
            getActiveProcess().getPcb().setProcessState(PCB.ProcessState.READY);
            return true;
        }

        public boolean spawnChild(){
            return true;
        }

        public boolean swapToIoWait() {
            getActiveProcess().getPcb().setProcessState(PCB.ProcessState.WAIT);
            return true;
        }

        public boolean startProcComm() {
            return true;
        }

        public boolean launchCriticalSection() throws InterruptedException{
            boolean result = false;

            try{
                mutex.acquire();

                try{

                    int prgCntr = getActiveProcess().getPcb().getProgramCounter();
                    ArrayList<String> prgText = getActiveProcess().getPcb().getInstructions();
                    String instruction = prgText.get(prgCntr);
                    int remainingCycles = Integer.parseInt(instruction.substring(instruction.indexOf(".")+1,instruction.lastIndexOf(".")));

                    if(remainingCycles > 1){
                        remainingCycles -= 1;
                        instruction = "CRITICAL." + remainingCycles + ".";
                        prgText.set(prgCntr, instruction);
                        getActiveProcess().getPcb().setInstructions(prgText);
                    } else if(remainingCycles == 1){
                        instruction = "FINISHED.";
                        prgText.set(prgCntr, instruction);
                        prgCntr ++;
                        getActiveProcess().getPcb().setInstructions(prgText);
                        getActiveProcess().getPcb().setProgramCounter(prgCntr);

                        if(prgCntr == prgText.size()) {
                            result = true;
                            getActiveProcess().getPcb().setProcessState(PCB.ProcessState.EXIT);
                        }
                    }

                } finally {
                    mutex.release();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;

        }

        public boolean checkForPageLoad(){
            boolean load;

            if(getActiveProcess().getPcb().getPageLoadIncr() > 0){
                getActiveProcess().getPcb().setPageLoadNeeded(true);
                load = true;
            } else {
                if(this.iterCount % Math.abs(getActiveProcess().getPcb().getPageLoadIncr()) == 0 && this.iterCount <= (getActiveProcess().getPcb().getMemoryReq())*Math.abs(getActiveProcess().getPcb().getPageLoadIncr())) {
                    getActiveProcess().getPcb().setPageLoadNeeded(true);
                    load = true;
                } else {
                    load = false;
                    getActiveProcess().getPcb().setPageLoadNeeded(false);
                }
            }

            setIterCount(getIterCount()+1);
            getActiveProcess().getPcb().setLastCpuIter(getIterCount());
            return load;
        }

        public Process getActiveProcess() {
            return activeProcess;
        }

        public void setActiveProcess(Process activeProcess) {
            this.activeProcess = activeProcess;
        }

        public void setIterCount(int iterCount) {
            this.iterCount = iterCount;
        }

        public int getIterCount() {
            return iterCount;
        }

    }
}
