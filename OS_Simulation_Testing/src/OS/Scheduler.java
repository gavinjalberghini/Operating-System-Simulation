package OS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * @author  Gavin Alberghini
 * @version 1.2
 * @since 1.0
 */

public class Scheduler {

    //TRACKING (Queues)
    MultiLevelFeedbackQueue readyQueue = new MultiLevelFeedbackQueue();
    ArrayList<Process> jobQueue = new ArrayList<>();
    ArrayList<Integer> terminatedPIDs = new ArrayList<>();
    ArrayList<Process> waitingForIoQueue = new ArrayList<>();
    ArrayList<Process> waitingForMemoryQueue = new ArrayList<>();
    private int iterationCount = 0;
    private boolean isSortOne;

    public Scheduler(boolean sort1){
        isSortOne = sort1;
    }

    public void iterate(){

        if(getWaitingForIoQueue().size() > 0) {
            Process activeProc = getWaitingForIoQueue().get(0);
            int prgCntr = activeProc.getPcb().getProgramCounter();
            ArrayList<String> prgText = activeProc.getPcb().getInstructions();
            String instruction = prgText.get(prgCntr);

            int remainingCycles = Integer.parseInt(instruction.substring(instruction.indexOf(".") + 1, instruction.lastIndexOf(".")));

            if (remainingCycles > 1) {
                remainingCycles -= 8; //keep pace w/ cpu

                if(remainingCycles < 1)
                    remainingCycles = 1;

                instruction = "I/O." + remainingCycles + ".";
                prgText.set(prgCntr, instruction);
                activeProc.getPcb().setInstructions(prgText);
                getWaitingForIoQueue().set(0,activeProc);
            } else if (remainingCycles == 1) {
                instruction = "FINISHED.";
                prgText.set(prgCntr, instruction);
                prgCntr++;
                activeProc.getPcb().setInstructions(prgText);
                activeProc.getPcb().setProgramCounter(prgCntr);

                if (prgCntr == prgText.size()){
                    activeProc.getPcb().setProcessState(PCB.ProcessState.EXIT);
                    getReadyQueue().assignProcessToMultiQueue(waitingForIoQueue.remove(0));
                } else {
                    activeProc.getPcb().setProcessState(PCB.ProcessState.READY);
                    getWaitingForIoQueue().set(0,activeProc);
                    getReadyQueue().assignProcessToMultiQueue(waitingForIoQueue.remove(0));
                }
            }
        }

        ageWaitingPriority();
        Collections.sort(this.waitingForMemoryQueue, new PComparator());
        getReadyQueue().updateReadyQueue(isSortOne);

    }

    public boolean isSortOne() {
        return isSortOne;
    }

    public void setSortOne(boolean sortOne) {
        isSortOne = sortOne;
    }

    //JOB QUEUE
    public ArrayList<Process> getJobQueue() {
        return jobQueue;
    }

    public void setJobQueue(ArrayList<Process> jobQueue) {
        this.jobQueue = jobQueue;
    }

    public void addToJobQueue(Process p){
        this.jobQueue.add(p);
    }

    public void removeFromJobQueue(int pid){
        for(int i=this.jobQueue.size()-1; i >= 0; --i){
            if(jobQueue.get(i).getPcb().getPid() == pid)
                jobQueue.remove(i);
        }
    }

    //TERMINATED QUEUE
    public ArrayList<Integer> getTerminatedPIDs() {
        return terminatedPIDs;
    }

    public void setTerminatedPIDs(ArrayList<Integer> terminatedPIDs) {
        this.terminatedPIDs = terminatedPIDs;
    }

    public void addToTerminatedQueue(int pid){
        this.terminatedPIDs.add(pid);
    }

    public void removeFromTerminatedQueue(int pid){
        this.terminatedPIDs.remove(Integer.valueOf(pid));
    }

    //IO WAITING QUEUE
    public ArrayList<Process> getWaitingForIoQueue() {
        return waitingForIoQueue;
    }

    public void setWaitingForIoQueue(ArrayList<Process> waitingForIoQueue) {
        this.waitingForIoQueue = waitingForIoQueue;
    }

    public void addToWaitingForIoQueue(Process p){
        this.waitingForIoQueue.add(p);
    }

    public void removeFromWaitingForIoQueue(int pid){
        for(int i=this.waitingForIoQueue.size()-1; i >= 0; --i){
            if(waitingForIoQueue.get(i).getPcb().getPid() == pid)
                waitingForIoQueue.remove(i);
        }
    }

    public boolean containsInIo(int pid){
        for(int i=getWaitingForIoQueue().size()-1; i >= 0; --i){
            if(getWaitingForIoQueue().get(i).getPcb().getPid() == pid)
                return true;
        }
        return false;
    }

    public Process getCopyFromIoQueue(int pid){
        for(int i=getWaitingForIoQueue().size()-1; i >= 0; --i){
            if(getWaitingForIoQueue().get(i).getPcb().getPid() == pid)
                return getWaitingForIoQueue().get(i);
        }

        return null;
    }

    //MEMORY WAITING QUEUE
    public ArrayList<Process> getWaitingForMemoryQueue() {
        return waitingForMemoryQueue;
    }

    public void setWaitingForMemoryQueue(ArrayList<Process> waitingForMemoryQueue) {
        this.waitingForMemoryQueue = waitingForMemoryQueue;
    }

    public void addToWaitingForMemoryQueue(Process p){
        this.waitingForMemoryQueue.add(p);
    }

    public void removeFromWaitingForMemoryQueue(int pid){
        for(int i=this.waitingForMemoryQueue.size()-1; i >= 0; --i){
            if(waitingForMemoryQueue.get(i).getPcb().getPid() == pid)
                waitingForMemoryQueue.remove(i);
        }
    }

    public void ageWaitingPriority(){

        if(this.iterationCount % 500 == 0) {
            for (int i = 0; i < getWaitingForMemoryQueue().size(); i++) {
                Process p = getWaitingForMemoryQueue().get(i);

                if (p.getPcb().getPriority() > 5)
                    p.getPcb().setPriority(p.getPcb().getPriority() - 1);

                getWaitingForMemoryQueue().set(i, p);
            }
        }

    }

    public boolean containsInMemoryWait(int pid){
        for(int i=getWaitingForMemoryQueue().size()-1; i >= 0; --i){
            if(getWaitingForMemoryQueue().get(i).getPcb().getPid() == pid)
                return true;
        }
        return false;
    }

    public Process getCoptFromMemoryWait(int pid){
        for(int i=getWaitingForMemoryQueue().size()-1; i >= 0; --i){
            if(getWaitingForMemoryQueue().get(i).getPcb().getPid() == pid)
                return getWaitingForMemoryQueue().get(i);
        }

        return null;
    }

    public MultiLevelFeedbackQueue getReadyQueue() {
        return readyQueue;
    }

    public void setReadyQueue(MultiLevelFeedbackQueue readyQueue) {
        this.readyQueue = readyQueue;
    }

    public String toString() {
        String result = getReadyQueue().toString();

        result += "RESOURCE WAITING QUEUE\n";

        for(Process p : getWaitingForMemoryQueue()) {
            result += "Process " + p.getPcb().getPid() + " : Priority " + p.getPcb().getPriority() + " : Mem needed " + p.getPcb().getMemoryReq() + " : is child " + p.getPdt().isChild();
            result += " : Resource One Needed " + p.getPcb().getResourceOneReq() + " : Resource Two Needed " + p.getPcb().getResourceTwoReq() + "\n";
        }

        result += "I/O WAITING QUEUE\n";

        for(Process p : getWaitingForIoQueue())
            result += "Process " + p.getPcb().getPid() + " : Waiting for " + p.getPcb().getInstructions().get(p.getPcb().getProgramCounter()) + "\n";

        return result;
    }

    //SCHEDULING ALGORITHMS
    class PComparator implements Comparator<Process> {
        public int compare(Process p1, Process p2) {
            return p1.getPcb().getPriority() - p2.getPcb().getPriority();
        }
    }

    class SJFComparator implements Comparator<Process> {
        public int compare(Process p1, Process p2) { return p1.getPcb().getInstructions().size() - p2.getPcb().getInstructions().size(); }
    }

    class SRTFComparator implements  Comparator<Process>{
        public int compare(Process p1, Process p2) { return (p1.getPcb().getInstructions().size()-p1.getPcb().getProgramCounter()) - (p2.getPcb().getInstructions().size()-p2.getPcb().getProgramCounter()); }
    }

    //MULTILEVEL FEEDBACK QUEUE
    class MultiLevelFeedbackQueue {

        ArrayList<Process> foregroundQueue = new ArrayList<>();
        ArrayList<Process> interQueue = new ArrayList<>();
        ArrayList<Process> backgroundQueue = new ArrayList<>();
        int window = 100;
        int iterCount = 0;
        int queueBoundOne = 50;
        int queueBoundTwo = 150;
        int foreProb = 5;
        int interProb = 8;
        int size = 0;

        //META INFO
        public boolean contains(int pid){
            boolean hasProcess;

            hasProcess = containsInBack(pid);
            if(hasProcess)
                return true;

            hasProcess = containsInInter(pid);
            if(hasProcess)
                return true;

            hasProcess = containsInFore(pid);
            if(hasProcess)
                return true;

            return false;
        }

        public Process getProcessCopyFromReadyQueue(int pid){
            Process copy;

            copy = getCopyFromForegroundQueue(pid);
            if(copy != null)
                return copy;

            copy = getCopyFromInterQueue(pid);
            if(copy != null)
                return copy;

            copy = getCopyFromBackgroundQueue(pid);
            if(copy != null)
                return copy;

            return null;
        }

        public int getForeProb() {
            return foreProb;
        }

        public void setForeProb(int foreProb) {
            this.foreProb = foreProb;
        }

        public int getInterProb() {
            return interProb;
        }

        public void setInterProb(int interProb) {
            this.interProb = interProb;
        }

        public void setSize(int size){
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public void setIterCount(int iterCount) {
            this.iterCount = iterCount;
        }

        public int getIterCount() {
            return iterCount;
        }

        public int getQueueBoundOne() {
            return queueBoundOne;
        }

        public void setQueueBoundOne(int queueBoundOne) {
            this.queueBoundOne = queueBoundOne;
        }

        public int getQueueBoundTwo() {
            return queueBoundTwo;
        }

        public void setQueueBoundTwo(int queueBoundTwo) {
            this.queueBoundTwo = queueBoundTwo;
        }

        public int getWindow() {
            return window;
        }

        public void setWindow(int window) {
            this.window = window;
        }

        //BACKGROUND QUEUE
        public ArrayList<Process> getBackgroundQueue() {
            return backgroundQueue;
        }

        public void setBackgroundQueue(ArrayList<Process> backgroundQueue) {
            this.backgroundQueue = backgroundQueue;
        }

        public void addToBackgroundQueue(Process p){
            this.backgroundQueue.add(p);
        }

        public void removeFromBackgroundQueue(int pid){
            for(int i=this.backgroundQueue.size()-1; i >= 0; --i){
                if(backgroundQueue.get(i).getPcb().getPid() == pid)
                    backgroundQueue.remove(i);
            }
        }

        public Process getCopyFromBackgroundQueue(int pid){
            for(int i=this.backgroundQueue.size()-1; i >= 0; --i){
                if(backgroundQueue.get(i).getPcb().getPid() == pid)
                    return backgroundQueue.get(i);
            }

            return null;
        }

        public boolean containsInBack(int pid){
            for(int i=this.backgroundQueue.size()-1; i >= 0; --i){
                if(backgroundQueue.get(i).getPcb().getPid() == pid)
                    return true;
            }
            return false;
        }

        public void updateInBackgroundQueue(int index, Process p){
            this.backgroundQueue.set(index,p);
        }

        //FOREGROUND QUEUE
        public ArrayList<Process> getForegroundQueue() {
            return foregroundQueue;
        }

        public void setForegroundQueue(ArrayList<Process> foregroundQueue) {
            this.foregroundQueue = foregroundQueue;
        }

        public void addToForegroundQueue(Process p){
            this.foregroundQueue.add(p);
        }

        public void removeFromForegroundQueue(int pid){
            for(int i=this.foregroundQueue.size()-1; i >= 0; --i){
                if(foregroundQueue.get(i).getPcb().getPid() == pid)
                    foregroundQueue.remove(i);
            }
        }

        public Process getCopyFromForegroundQueue(int pid){
            for(int i=this.foregroundQueue.size()-1; i >= 0; --i){
                if(foregroundQueue.get(i).getPcb().getPid() == pid)
                    return foregroundQueue.get(i);
            }

            return null;
        }

        public boolean containsInFore(int pid){
            for(int i=this.foregroundQueue.size()-1; i >= 0; --i){
                if(foregroundQueue.get(i).getPcb().getPid() == pid)
                    return true;
            }
            return false;
        }

        public void updateInForegroundQueue(int index, Process p){
            this.foregroundQueue.set(index,p);
        }

        //INTER QUEUE
        public ArrayList<Process> getInterQueue() {
            return interQueue;
        }

        public void setInterQueue(ArrayList<Process> interQueue) {
            this.interQueue = interQueue;
        }

        public void addToInterQueue(Process p){
            this.interQueue.add(p);
        }

        public void removeFromInterQueue(int pid){
            for(int i=this.interQueue.size()-1; i >= 0; --i){
                if(interQueue.get(i).getPcb().getPid() == pid)
                    interQueue.remove(i);
            }
        }

        public Process getCopyFromInterQueue(int pid){
            for(int i=this.interQueue.size()-1; i >= 0; --i){
                if(interQueue.get(i).getPcb().getPid() == pid)
                    return interQueue.get(i);
            }

            return null;
        }

        public boolean containsInInter(int pid){
            for(int i=this.interQueue.size()-1; i >= 0; --i){
                if(interQueue.get(i).getPcb().getPid() == pid)
                    return true;
            }
            return false;
        }

        public void updateInInterQueue(int index, Process p){
            this.interQueue.set(index,p);
        }

        public void assignProcessToMultiQueue(Process p){
            int priority = p.getPcb().getPriority();

            if(priority<getQueueBoundOne())
                addToForegroundQueue(p);
            else if(priority<getQueueBoundTwo())
                addToInterQueue(p);
            else
                addToBackgroundQueue(p);
        }

        public Process pop(){

            Process result;
            Random rand = new Random();
            int queueChoice = rand.nextInt(10)+1;
            int selection = 0;

            if(queueChoice<=getForeProb()){
                selection=1;
            } else if(queueChoice<=getForeProb()){
                selection=2;
            } else {
                selection=3;
            }

            switch(selection){
                case 1:
                    if(foregroundQueue.size()>0)
                        return foregroundQueue.remove(0);
                    else if(interQueue.size()>0)
                        return interQueue.remove(0);
                    else if(backgroundQueue.size()>0)
                        return backgroundQueue.remove(0);
                    else return null;
                case 2:
                    if(interQueue.size()>0)
                        return interQueue.remove(0);
                    else if(backgroundQueue.size()>0)
                        return backgroundQueue.remove(0);
                    else if(foregroundQueue.size()>0)
                        return foregroundQueue.remove(0);
                    else return null;
                case 3:
                    if(backgroundQueue.size()>0)
                        return backgroundQueue.remove(0);
                    else if(interQueue.size()>0)
                        return interQueue.remove(0);
                    else if(foregroundQueue.size()>0)
                        return foregroundQueue.remove(0);
                    else return null;
                default:
                    return null;
            }
        }

        public void updateReadyQueue(boolean sort1){

            setIterCount(getIterCount()+1);
            int count = getIterCount();
            setIterCount(count+1);
            Process temp;

            if(count%getWindow()==0){

                for(int i=0; i<getForegroundQueue().size(); i++) {
                    temp = getForegroundQueue().get(i);
                    if(temp.getPcb().getPriority() > 5)
                        temp.getPcb().setPriority(temp.getPcb().getPriority()-1);
                    updateInForegroundQueue(i,temp);
                }

                for(int i=0; i<getInterQueue().size(); i++) {
                    temp = getInterQueue().get(i);
                    if(temp.getPcb().getPriority() > 5)
                        temp.getPcb().setPriority(temp.getPcb().getPriority()-1);
                    updateInInterQueue(i,temp);
                }

                for(int i=0; i<getInterQueue().size(); i++) {
                    temp = getInterQueue().get(i);
                    if(temp.getPcb().getPriority() < getQueueBoundOne()){
                        addToForegroundQueue(temp);
                        removeFromInterQueue(temp.getPcb().getPid());
                    }
                }

                for(int i=0; i<getBackgroundQueue().size(); i++) {
                    temp = getInterQueue().get(i);
                    if(temp.getPcb().getPriority() > 5)
                        temp.getPcb().setPriority(temp.getPcb().getPriority()-1);
                    updateInBackgroundQueue(i,temp);
                }

                for(int i=0; i<getBackgroundQueue().size(); i++) {
                    temp = getInterQueue().get(i);
                    if(temp.getPcb().getPriority() < getQueueBoundTwo()){
                        addToInterQueue(temp);
                        removeFromBackgroundQueue(temp.getPcb().getPid());
                    }
                }
            }


            if(sort1) {
                Collections.sort(this.foregroundQueue, new PComparator());
                Collections.sort(this.interQueue, new SJFComparator());
                Collections.sort(this.backgroundQueue, new SRTFComparator());
            } else {
                Collections.sort(this.foregroundQueue, new SJFComparator());
                Collections.sort(this.interQueue, new SRTFComparator());
                Collections.sort(this.backgroundQueue, new PComparator());
            }

            setSize(this.foregroundQueue.size()+this.interQueue.size()+this.backgroundQueue.size());

        }

        public String toString(){
            String result = "";
            result += "READY QUEUE\n";
            result += "FOREGROUND QUEUE\n";

            for(Process p:getForegroundQueue()) {
                result += "Process " + p.getPcb().getPid() + " : Priority " + p.getPcb().getPriority() + " ";
                result += ": Instructions Remaining " + (p.getPcb().getInstructions().size()-p.getPcb().getProgramCounter()) + " ";
                result += ": JobType " + p.getPcb().getJobType() + "\n";
            }

            result += "INTER QUEUE\n";

            for(Process p:getInterQueue()) {
                result += "Process " + p.getPcb().getPid() + " : Size " + p.getPcb().getMemoryReq() + " ";
                result += ": Instructions Remaining " + (p.getPcb().getInstructions().size()-p.getPcb().getProgramCounter()) + " ";
                result += ": JobType " + p.getPcb().getJobType() + "\n";
            }

            result += "BACKGROUND\n";

            for(Process p:getBackgroundQueue()) {
                result += "Process " + p.getPcb().getPid() + " : Instructions Remaining " + (p.getPcb().getInstructions().size()-p.getPcb().getProgramCounter()) + " ";
                result += ": JobType " + p.getPcb().getJobType() + "\n";
            }

            return result;
        }

    }
}
