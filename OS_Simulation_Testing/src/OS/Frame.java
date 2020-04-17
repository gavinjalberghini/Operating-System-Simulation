package OS;

public class Frame {

    private static int addressBuilder = 1;

    //ORGINIZATION
    int physicalAddress;
    int size = 1; //1 Megabyte

    //CONTENT
    int proccessID = -1;

    //STATUS
    boolean isFree = true;
    boolean isVMFrame = false;

    public Frame(){
        generateAndSetPhysicalAddress();
    }

    public void setPhysicalAddress(int physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public int getPhysicalAddress() {
        return physicalAddress;
    }

    public int getSize() {
        return size;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public boolean isFree() {
        return isFree;
    }

    public void occupy(int pid){
        this.isFree = false;
        this.proccessID = pid;
    }

    public void free(){
        this.isFree = true;
        this.proccessID = -1;
    }

    public void setVMFrame(boolean VMFrame) {
        isVMFrame = VMFrame;
    }

    public boolean isVMFrame() {
        return isVMFrame;
    }

    public int getProccessID() {
        return proccessID;
    }

    public void setProccessID(int proccessID) {
        this.proccessID = proccessID;
    }

    public static int getAddressBuilder() {
        return addressBuilder;
    }

    public static void setAddressBuilder(int addressBuilder) {
        Frame.addressBuilder = addressBuilder;
    }

    public void generateAndSetPhysicalAddress(){
        setPhysicalAddress(getAddressBuilder());
        setAddressBuilder(getAddressBuilder()+1);
    }

    public String toString(){
        String result = "";

        result += "Physical Addr: " + this.getPhysicalAddress() + "\n";
        result += "Contents: " + this.getProccessID();

        return result;
    }
}
