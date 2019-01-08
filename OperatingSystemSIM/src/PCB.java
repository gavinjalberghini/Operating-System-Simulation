import java.util.ArrayList;
import java.util.Random;


public class PCB {

    private static int baseRegBuilder = 0;

    enum ProcessState{
        NEW,READY,WAIT,RUN,EXIT
    }

    //GENERIC PROCESS INFO
    int pid = 0;
    int programCounter = 0;
    int totalCpuCyclesUsed = 0;
    int priority = 100;
    String jobType = "";
    ArrayList<String> instructions = new ArrayList<>();

    //BASE & LIMIT REGISTERS
    int baseRegister = 0;
    int limitRegister = 0;

    //PAGE MANAGING
    int pageCtr = 0;
    int numOfPages = 0;
    int lastCpuIter = 0;
    int pageLoadIncr = 0;
    int nextLoadAmnt = 0;
    int offset = 1;

    //REQUIREMENTS TO RUN
    int memoryReq = 0;
    int resourceOneReq = 0;
    int resourceTwoReq = 0;

    //IPC MANAGEMENT
    int mailKey = 0;
    String mailData = "";

    //STATUS MONITORS
    boolean hasProcessResources = false;
    boolean hasMemory = false;
    boolean inRamOnly = false;
    boolean pageLoadNeeded = false;
    ProcessState processState = ProcessState.NEW;

    public PCB(){

    }

    public static int getBaseRegBuilder() {
        return baseRegBuilder;
    }

    public static void setBaseRegBuilder(int baseRegBuilder) {
        PCB.baseRegBuilder = baseRegBuilder;
    }

    public int getPid(){
        return this.pid;
    }

    public void setPid(int pid){
        this.pid = pid;
    }

    public int getProgramCounter(){
        return this.programCounter;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public int getTotalCpuCyclesUsed(){
        return this.totalCpuCyclesUsed;
    }

    public void setTotalCpuCyclesUsed(int totalCpuCyclesUsed){
        this.totalCpuCyclesUsed = totalCpuCyclesUsed;
    }

    public int getPriority(){
        return this.priority;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public String getJobType(){
        return this.jobType;
    }

    public void setJobType(String jobType){
        this.jobType = jobType;
    }

    public ArrayList<String> getInstructions(){
        return this.instructions;
    }

    public void setInstructions(ArrayList<String> instructions){
        this.instructions = instructions;
    }

    public int getBaseRegister(){
        return this.baseRegister;
    }

    public void setBaseRegister(int baseRegister) {
        this.baseRegister = baseRegister;
    }

    public int getLimitRegister() {
        return limitRegister;
    }

    public void setLimitRegister(int limitRegister) {
        this.limitRegister = limitRegister;
    }

    public void setPageCtr(int pageCtr) {
        this.pageCtr = pageCtr;
    }

    public int getPageCtr() {
        return pageCtr;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(int numOfPages) {
        this.numOfPages = numOfPages;
    }

    public int getLastCpuIter() {
        return lastCpuIter;
    }

    public void setLastCpuIter(int lastCpuIter) {
        this.lastCpuIter = lastCpuIter;
    }

    public int getPageLoadIncr() {
        return pageLoadIncr;
    }

    public void setPageLoadIncr(int pageLoadIncr) {
        this.pageLoadIncr = pageLoadIncr;
    }

    public int getNextLoadAmnt() {
        return nextLoadAmnt;
    }

    public void setNextLoadAmnt(int nextLoadAmnt) {
        this.nextLoadAmnt = nextLoadAmnt;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getMemoryReq() {
        return memoryReq;
    }

    public void setMemoryReq(int memoryReq) {
        this.memoryReq = memoryReq;
    }

    public int getResourceOneReq() {
        return resourceOneReq;
    }

    public void setResourceOneReq(int resourceOneReq) {
        this.resourceOneReq = resourceOneReq;
    }

    public int getResourceTwoReq() {
        return resourceTwoReq;
    }

    public void setResourceTwoReq(int resourceTwoReq) {
        this.resourceTwoReq = resourceTwoReq;
    }

    public int getMailKey() {
        return mailKey;
    }

    public void setMailKey(int mailKey) {
        this.mailKey = mailKey;
    }

    public String getMailData() {
        return mailData;
    }

    public void setMailData(String mailData) {
        this.mailData = mailData;
    }

    public boolean hasProcessResources() {
        return hasProcessResources;
    }

    public void setHasProcessResources(boolean hasProcessResources) {
        this.hasProcessResources = hasProcessResources;
    }

    public boolean hasMemory() {
        return hasMemory;
    }

    public void setHasMemory(boolean hasMemory) {
        this.hasMemory = hasMemory;
    }

    public boolean isInRamOnly() {
        return inRamOnly;
    }

    public void setInRamOnly(boolean inRamOnly) {
        this.inRamOnly = inRamOnly;
    }

    public boolean isPageLoadNeeded() {
        return pageLoadNeeded;
    }

    public void setPageLoadNeeded(boolean pageLoadNeeded) {
        this.pageLoadNeeded = pageLoadNeeded;
    }

    public ProcessState getProcessState() {
        return processState;
    }

    public void setProcessState(ProcessState processState) {
        this.processState = processState;
    }

    public void generateAndSetPid(){
        Random generate = new Random();
        setPid(generate.nextInt(899999)+100000);
    }

    public void generateAndSetLogicalMemory(){
        setBaseRegister(baseRegBuilder);
        setLimitRegister(this.memoryReq);
        setBaseRegBuilder(getBaseRegBuilder()+getLimitRegister());
        setPageCtr(getBaseRegister());
    }

    public void shiftToNextInstruction(){
        getInstructions().set(getProgramCounter(),"FINISHED.");
        setProgramCounter(getProgramCounter()+1);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
