import java.util.HashMap;
import java.util.Stack;

public class Memory {

    //TRACKING
    HashMap<Integer, Frame> frames = new HashMap<Integer, Frame>();
    Stack<Frame> freeFrames = new Stack<Frame>();

    //STATUS
    int numOfFreeFrames;
    boolean isFreeMemory = true;

    //META
    int size;

    public Memory(boolean isVM, int size){
        initializeMemory(isVM, size);
    }

    public void setFrames(HashMap<Integer, Frame> frames) {
        this.frames = frames;
    }

    public HashMap<Integer, Frame> getFrames() {
        return frames;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setFreeMemory(boolean freeMemory) {
        isFreeMemory = freeMemory;
    }

    public boolean isFreeMemory() {
        return isFreeMemory;
    }

    public int getNumOfFreeFrames() {
        return numOfFreeFrames;
    }

    public void setNumOfFreeFrames(int numOfFreeFrames) {
        this.numOfFreeFrames = numOfFreeFrames;
    }

    public Stack<Frame> getFreeFrames() {
        return freeFrames;
    }

    public void setFreeFrames(Stack<Frame> freeFrames) {
        this.freeFrames = freeFrames;
    }

    public void addToFrames(Frame frame){
        this.frames.put(frame.getPhysicalAddress(), frame);
    }

    public void removeFromFrames(Frame frame){
        this.frames.remove(frame.getPhysicalAddress(), frame);
    }

    public void removeFromFramesByAddr(int physicalAddr){
        this.frames.remove(physicalAddr);
    }

    public void updateFrame(Frame frame){
        this.frames.put(frame.getPhysicalAddress(), frame);
    }

    public void addToFreeFrames(Frame frame){
        this.freeFrames.push(frame);
    }

    public void removeFromStack(Frame frame){
        this.freeFrames.remove(frame);
    }

    public Frame getNextFreeFrame(){
        return this.freeFrames.pop();
    }

    public Frame allocateFrame(int pid){

        Frame victim = getNextFreeFrame();
        victim.occupy(pid);
        updateFrame(victim);
        updateNumOfFreeFrames();
        return victim;

    }

    public void updateNumOfFreeFrames(){

        setNumOfFreeFrames(getFreeFrames().size());

    }

    public void deallocateFrame(int physicalAddr){

        Frame released = this.frames.get(physicalAddr);
        released.free();
        updateFrame(released);
        getFreeFrames().push(released);
        updateNumOfFreeFrames();

    }

    private void initializeMemory(boolean isVM, int size){

        setSize(size);

        for(int i=0; i<size; i++){
            Frame newFrame = new Frame();

            if(isVM)
                newFrame.setVMFrame(true);

            addToFrames(newFrame);
            addToFreeFrames(newFrame);
        }

        updateNumOfFreeFrames();
    }

    @Override
    public String toString() {

        String result = "";

        int count = 1;
        for(Frame f : frames.values()){
            if(count%4==0)
                result += "Frame " + f.getPhysicalAddress() + " occupied by " + f.getProccessID() + "\n";
            else
                result += "Frame " + f.getPhysicalAddress() + " occupied by " + f.getProccessID() + " | ";

            count++;
        }

        return result;
    }
}
