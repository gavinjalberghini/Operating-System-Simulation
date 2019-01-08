import java.util.ArrayList;

public class Debugger {

    public Debugger(){

    }

    public void displayProcessInfo(ArrayList<Process> proc){
        for(Process p:proc)
            System.out.println(p);
    }

    public void displayProcessInfo(Process p){
        System.out.println(p);
    }

    public String displayMemory(Memory ram, Memory vm){
        return "RAM \n\n" + ram.toString() + " \n\n Virtual Memory\n\n" + vm.toString();
    }

    public String displayCPU(CPU cpu){
        return cpu.toString();
    }

    public String displayScheduler(Scheduler scheduler){

        return scheduler.toString();

    }

}
