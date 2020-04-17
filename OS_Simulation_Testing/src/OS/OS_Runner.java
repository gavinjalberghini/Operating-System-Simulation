package OS;

import java.awt.*;
import java.util.Random;

public class OS_Runner {

    public static void main(String[] args){

        var ex = new ControlGUI();
        EventQueue.invokeLater(() -> {
            ex.showGUI();
        });

        boolean s1;

        String command = "Pass()";
        while(command.equalsIgnoreCase("Pass()")){
            command = ex.getCommandLineString();

            try {
                Thread.sleep(200);
            } catch (Exception e){

            }

        }

        if(command.equalsIgnoreCase("SchTwo()"))
            s1=false;
        else
            s1=true;

        OS operatingSystem = new OS(s1);
        Random rand = new Random();

        int prop =  (int)(100*((double)(6000-operatingSystem.getCpu().getMmu().getResourceOne())/(double)(6000)));
        int prtp = (int)(100*((double)(6000-operatingSystem.getCpu().getMmu().getResourceTwo())/(double)(6000)));
        int ramp = (int)(100*((double)(operatingSystem.getCpu().getMmu().getRam().getSize() - operatingSystem.getCpu().getMmu().getRam().getNumOfFreeFrames())/(double)operatingSystem.getCpu().getMmu().getRam().getSize()));
        int vmp = (int)(100*((double)(operatingSystem.getCpu().getMmu().getVirtualMemory().getSize() - operatingSystem.getCpu().getMmu().getVirtualMemory().getNumOfFreeFrames())/(double)operatingSystem.getCpu().getMmu().getVirtualMemory().getSize()));
        String mainConsoleText = operatingSystem.getDebugger().displayScheduler(operatingSystem.getScheduler());
        String cpuConsoleText = operatingSystem.getDebugger().displayCPU(operatingSystem.getCpu());
        ex.update(prop,prtp,ramp,vmp,mainConsoleText,cpuConsoleText);
        ex.setEventConsole(operatingSystem.getMailBox().toString());

        while(!command.substring(0,command.indexOf('(')).equalsIgnoreCase("Quit")) {

            if(command.substring(0,command.indexOf('(')).equalsIgnoreCase("Run")){
                for (int i = 0; i < Integer.parseInt(command.substring(command.indexOf('(')+1, command.indexOf(')'))); i++) {

                    if(rand.nextInt(15000) == 150){
                        ex.setEventConsole("SYSTEM I/O INTERRUPT");

                        try{
                            Thread.sleep(800);
                        } catch(Exception e) {

                        }
                    }

                    operatingSystem.launchCommand("Run()");
                    prop =  (int)(100*((double)(6000-operatingSystem.getCpu().getMmu().getResourceOne())/(double)(6000)));
                    prtp = (int)(100*((double)(6000-operatingSystem.getCpu().getMmu().getResourceTwo())/(double)(6000)));
                    ramp = (int)(100*((double)(operatingSystem.getCpu().getMmu().getRam().getSize() - operatingSystem.getCpu().getMmu().getRam().getNumOfFreeFrames())/(double)operatingSystem.getCpu().getMmu().getRam().getSize()));
                    vmp = (int)(100*((double)(operatingSystem.getCpu().getMmu().getVirtualMemory().getSize() - operatingSystem.getCpu().getMmu().getVirtualMemory().getNumOfFreeFrames())/(double)operatingSystem.getCpu().getMmu().getVirtualMemory().getSize()));
                    mainConsoleText = operatingSystem.getDebugger().displayScheduler(operatingSystem.getScheduler());
                    cpuConsoleText = operatingSystem.getDebugger().displayCPU(operatingSystem.getCpu());
                    ex.update(prop,prtp,ramp,vmp,mainConsoleText,cpuConsoleText);
                    ex.setEventConsole(operatingSystem.getMailBox().toString());
                }
            } else if (command.substring(0,command.indexOf('(')).equalsIgnoreCase("Quit")) {
                System.exit(0);
                break;
            } else if (command.substring(0,command.indexOf('(')).equalsIgnoreCase("Out")) {
                prop =  (int)(100*((double)(6000-operatingSystem.getCpu().getMmu().getResourceOne())/(double)(6000)));
                prtp = (int)(100*((double)(6000-operatingSystem.getCpu().getMmu().getResourceTwo())/(double)(6000)));
                ramp = (int)(100*((double)(operatingSystem.getCpu().getMmu().getRam().getSize() - operatingSystem.getCpu().getMmu().getRam().getNumOfFreeFrames())/(double)operatingSystem.getCpu().getMmu().getRam().getSize()));
                vmp = (int)(100*((double)(operatingSystem.getCpu().getMmu().getVirtualMemory().getSize() - operatingSystem.getCpu().getMmu().getVirtualMemory().getNumOfFreeFrames())/(double)operatingSystem.getCpu().getMmu().getVirtualMemory().getSize()));
                mainConsoleText = operatingSystem.getDebugger().displayScheduler(operatingSystem.getScheduler());
                cpuConsoleText = operatingSystem.getDebugger().displayCPU(operatingSystem.getCpu());
                ex.update(prop,prtp,ramp,vmp,mainConsoleText,cpuConsoleText);
                ex.setEventConsole(operatingSystem.getMailBox().toString());
            } else if (command.substring(0,command.indexOf('(')).equalsIgnoreCase("MemOut")) {
                prop =  (int)(100*((double)(6000-operatingSystem.getCpu().getMmu().getResourceOne())/(double)(6000)));
                prtp = (int)(100*((double)(6000-operatingSystem.getCpu().getMmu().getResourceTwo())/(double)(6000)));
                ramp = (int)(100*((double)(operatingSystem.getCpu().getMmu().getRam().getSize() - operatingSystem.getCpu().getMmu().getRam().getNumOfFreeFrames())/(double)operatingSystem.getCpu().getMmu().getRam().getSize()));
                vmp = (int)(100*((double)(operatingSystem.getCpu().getMmu().getVirtualMemory().getSize() - operatingSystem.getCpu().getMmu().getVirtualMemory().getNumOfFreeFrames())/(double)operatingSystem.getCpu().getMmu().getVirtualMemory().getSize()));
                mainConsoleText = operatingSystem.getDebugger().displayMemory(operatingSystem.getCpu().getMmu().getRam(),operatingSystem.getCpu().getMmu().getVirtualMemory());
                cpuConsoleText = operatingSystem.getDebugger().displayCPU(operatingSystem.getCpu());
                ex.update(prop,prtp,ramp,vmp,mainConsoleText,cpuConsoleText);
                ex.setEventConsole(operatingSystem.getMailBox().toString());
            } else if (command.substring(0,command.indexOf('(')).equalsIgnoreCase("Rand")) {
                operatingSystem.launchCommand(command);
                prop =  (int)(100*((double)(6000-operatingSystem.getCpu().getMmu().getResourceOne())/(double)(6000)));
                prtp = (int)(100*((double)(6000-operatingSystem.getCpu().getMmu().getResourceTwo())/(double)(6000)));
                ramp = (int)(100*((double)(operatingSystem.getCpu().getMmu().getRam().getSize() - operatingSystem.getCpu().getMmu().getRam().getNumOfFreeFrames())/(double)operatingSystem.getCpu().getMmu().getRam().getSize()));
                vmp = (int)(100*((double)(operatingSystem.getCpu().getMmu().getVirtualMemory().getSize() - operatingSystem.getCpu().getMmu().getVirtualMemory().getNumOfFreeFrames())/(double)operatingSystem.getCpu().getMmu().getVirtualMemory().getSize()));
                mainConsoleText = operatingSystem.getDebugger().displayScheduler(operatingSystem.getScheduler());
                cpuConsoleText = operatingSystem.getDebugger().displayCPU(operatingSystem.getCpu());
                ex.update(prop,prtp,ramp,vmp,mainConsoleText,cpuConsoleText);
                ex.setEventConsole(operatingSystem.getMailBox().toString());

            } else if (command.substring(0,command.indexOf('(')).equalsIgnoreCase("LookUp")) {
                operatingSystem.launchCommand(command);
                ex.setProcessLookUp(operatingSystem.getLookUp().toString());
                ex.setEventConsole(operatingSystem.getMailBox().toString());
            }

            command = "Pass()";
            while(command.equalsIgnoreCase("Pass()")){
                command = ex.getCommandLineString();

                try {
                    Thread.sleep(200);
                } catch (Exception e){

                }

            }

            ex.commandAquired();
        }

        System.exit(0);

    }

}
