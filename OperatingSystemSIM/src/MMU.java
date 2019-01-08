import java.util.HashMap;

public class MMU {

    //TRACKING
    HashMap<Integer, Integer> memoryMap = new HashMap<>();
    HashMap<Integer,Integer> cache = new HashMap<>();
    Memory virtualMemory;
    Memory ram;

    //PROCESS RESOURCES
    int resourceOne = 6000;
    int resourceTwo = 6000;

    public MMU(int ramSize, int vmSize){

        this.ram = new Memory(false, ramSize);
        this.virtualMemory = new Memory(true, vmSize);

    }

    public HashMap<Integer, Integer> getMemoryMap() {
        return memoryMap;
    }

    public void setMemoryMap(HashMap<Integer, Integer> memoryMap) {
        this.memoryMap = memoryMap;
    }

    public void updateMemoryMap(int logical, int physical){
        getMemoryMap().put(logical,physical);
    }

    public HashMap<Integer, Integer> getCache() {
        return cache;
    }

    public void setCache(HashMap<Integer, Integer> cache) {
        this.cache = cache;
    }

    public void updateCache(int logical, int physical){
        getCache().put(logical, physical);
    }

    public Memory getRam() {
        return ram;
    }

    public Memory getVirtualMemory() {
        return virtualMemory;
    }

    public void setRam(Memory ram) {
        this.ram = ram;
    }

    public void setVirtualMemory(Memory virtualMemory) {
        this.virtualMemory = virtualMemory;
    }

    public int getResourceOne() {
        return resourceOne;
    }

    public int getResourceTwo() {
        return resourceTwo;
    }

    public void setResourceOne(int resourceOne) {
        this.resourceOne = resourceOne;
    }

    public void setResourceTwo(int resourceTwo) {
        this.resourceTwo = resourceTwo;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}