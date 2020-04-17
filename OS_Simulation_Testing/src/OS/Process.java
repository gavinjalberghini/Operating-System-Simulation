package OS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Process {

    private PCB pcb;
    private PDT pdt;
    ArrayList<Pipe> pipes = new ArrayList<>();
    HashMap<Integer, Integer> pageTable = new HashMap<>();
    HashMap<Integer, Integer> validBits = new HashMap<>();

    public Process(){
        this.pcb = new PCB();
        this.pdt = new PDT();
    }

    public PCB getPcb() {
        return pcb;
    }

    public PDT getPdt() {
        return pdt;
    }

    public ArrayList<Pipe> getPipes() {
        return pipes;
    }

    public HashMap<Integer, Integer> getPageTable() {
        return pageTable;
    }

    public HashMap<Integer, Integer> getValidBits() {
        return validBits;
    }

    public void setPcb(PCB pcb) {
        this.pcb = pcb;
    }

    public void setPdt(PDT pdt) {
        this.pdt = pdt;
    }

    public void addToPageTable(int logical, int physical, int validBit){
        pageTable.put(logical, physical);
        validBits.put(logical, validBit);
        this.getPcb().setPageCtr(this.getPcb().getPageCtr() + 1);
    }

    public void buildPipe(int companionPid, int lock, int key){
        Pipe newPipe = new Pipe();
        newPipe.setConnectedProcPid(companionPid);
        newPipe.setPipeKey(key);
        newPipe.setPipeLock(lock);

        pipes.add(newPipe);
    }

    public class Pipe{

        public int personalMessage = 0;
        public int companionMessage = 0;
        int pipeLock = 0;
        int pipeKey = 0;
        int connectedProcessPid = 0;

        public void communicate(Process companion){

            Pipe connectionPipe = null;

            for(Pipe pipe:companion.getPipes())
                if(pipe.connectedProcessPid == getPcb().getPid())
                    connectionPipe = pipe;

            if(connectionPipe != null) {
                int message = connectionPipe.getMessage(this.useKey());
                if (message != -1)
                    this.companionMessage = message;
            }
        }

        public int getMessage(int key){
            if(key == this.pipeLock) {
                this.generateMessage();
                return this.personalMessage;
            } else {
                return -1;
            }
        }

        public int[] returnInfo(){
            return new int[] {this.connectedProcessPid, this.pipeKey, this.pipeLock, this.personalMessage, this.companionMessage};
        }

        public void setPipeLock(int lock){
            this.pipeLock = lock;
        }

        public void setPipeKey(int key){
            this.pipeKey = key;
        }

        public int useKey(){
            return this.pipeKey;
        }

        public void generateMessage(){
            Random generate = new Random();
            this.personalMessage = generate.nextInt(Integer.MAX_VALUE);
        }

        public void setConnectedProcPid(int pid){
            this.connectedProcessPid = pid;
        }


    }

    public String toString(){

        String result = "";
        result += "STATIC INFO:\n";
        result += "Job Type -- " + this.getPcb().getJobType() + " : ";
        result += "PID -- " + this.getPcb().getPid() + " : ";
        result += "Memory Requirement -- " + this.getPcb().getMemoryReq() + "\n";
        result += "Text block size -- " + this.getPcb().getInstructions().size() + " : ";
        result += "Base register -- " + this.getPcb().getBaseRegister() + " : ";
        result += "Limit register -- " + this.getPcb().getLimitRegister() + "\n";
        result += "Resource one req -- " + this.getPcb().getResourceOneReq() + " : ";
        result += "Resource two req -- " + this.getPcb().getResourceTwoReq() + " : \n\n";
        result += "DYNAMIC INFO:\n";
        result += "Current Program Pointer -- " + this.getPcb().getProgramCounter() + " : ";
        result += "Current Priority -- " + this.getPcb().getPriority() + " : ";
        result += "Current Process State -- " + this.getPcb().getProcessState() + "\n";
        result += "Has Process Resources -- " + this.getPcb().hasProcessResources() + "\n\n";
        result += "TEXT BLOCK:\n";

        int count = 1;
        for (String s:this.getPcb().getInstructions()) {
            result += s;

            if(count%5==0){
                result+="\n";
            } else {
                result+=" : ";
            }

            count ++;
        }

        result += "\nPAGE TABLE:\n";

        count = 1;
        for (Integer i:this.pageTable.keySet()) {
            result += i + " -- " + this.pageTable.get(i);

            if(count%5==0){
                result+="\n";
            } else {
                result+=" : ";
            }

            count ++;
        }

        result += "\nPIPES\n";

        for (Pipe p:pipes){
            result += "Pipe with process - " + p.connectedProcessPid + " : Companion Message - " + p.companionMessage +
                    "\n\t Personal Message - " + p.personalMessage + " : Lock - " + p.pipeLock + " : Key - " + p.pipeKey + "\n";
        }

        return result;
    }
}
