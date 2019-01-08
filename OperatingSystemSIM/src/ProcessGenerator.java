import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class ProcessGenerator {

    public ProcessGenerator(){

    }

    public ArrayList<Process> generateProcesses(int amount){
        ArrayList<Process> result = new ArrayList<>();
        Random generate = new Random();
        int randomJob;

        for(int i=0; i<amount; i++){
            randomJob = generate.nextInt(5)+1;
            switch(randomJob){
                case 1:
                    result.add(buildProcessFromTemplate("ProcessTemplates/MediaPlayer.xml"));
                    break;

                case 2:
                    result.add(buildProcessFromTemplate("ProcessTemplates/PhotoEditor.xml"));
                    break;

                case 3:
                    result.add(buildProcessFromTemplate("ProcessTemplates/VirusScanner.xml"));
                    break;

                case 4:
                    result.add(buildProcessFromTemplate("ProcessTemplates/WebBrowser.xml"));
                    break;

                case 5:
                    result.add(buildProcessFromTemplate("ProcessTemplates/WordProcessor.xml"));
                    break;
            }

        }

        return result;
    }

    public Process buildMediaPlayerProcess(){
        return buildProcessFromTemplate("ProcessTemplates/MediaPlayer.xml");
    }

    public Process buildWordProcessorProcess(){
        return buildProcessFromTemplate("ProcessTemplates/WordProcessor.xml");
    }

    public Process buildWebBrowserProcess(){
        return buildProcessFromTemplate("ProcessTemplates/WebBrowser.xml");
    }

    public Process buildPhotoEditorProcess(){
        return buildProcessFromTemplate("ProcessTemplates/PhotoEditor.xml");
    }

    public Process buildVirusScannerProcess(){
        return buildProcessFromTemplate("ProcessTemplates/VirusScanner.xml");
    }

    private static Process buildProcessFromTemplate(String fileName){
        Random generate = new Random();
        Process result = new Process();
        ArrayList<String> textBlock = new ArrayList<>();
        int calculateProbability=0, ioProbability=0, yieldProbability=0,
                childProbability=0, memoryRequirement=0, priority=0,
                interProcCommProbability=0, critSecProbability=0, pageLoadIter=0;
        String jobType = "";

        try {
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Process");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    jobType = eElement.getAttribute("Name");
                    calculateProbability = Integer.parseInt(eElement.getElementsByTagName("Calculate").item(0).getTextContent());
                    ioProbability = Integer.parseInt(eElement.getElementsByTagName("IO").item(0).getTextContent());
                    interProcCommProbability = Integer.parseInt(eElement.getElementsByTagName("InterProcessComm").item(0).getTextContent());
                    yieldProbability = Integer.parseInt(eElement.getElementsByTagName("Yield").item(0).getTextContent());
                    childProbability = Integer.parseInt(eElement.getElementsByTagName("Child").item(0).getTextContent());
                    critSecProbability = Integer.parseInt(eElement.getElementsByTagName("CriticalSection").item(0).getTextContent());
                    memoryRequirement = Integer.parseInt(eElement.getElementsByTagName("Memory").item(0).getTextContent());
                    priority = Integer.parseInt(eElement.getElementsByTagName("Priority").item(0).getTextContent());
                }
            }

            int calculate = 1, io = 2, yield = 3, procComm = 5, critSec = 0, count = 9;
            int[] probabilityArray = new int[10];

            while(count >= 0){

                if(calculateProbability>0){
                    probabilityArray[count] = calculate;
                    calculateProbability--;
                } else if(ioProbability>0){
                    probabilityArray[count] = io;
                    ioProbability--;
                } else if(yieldProbability>0){
                    probabilityArray[count] = yield;
                    yieldProbability--;
                } else if(interProcCommProbability>0){
                    probabilityArray[count] = procComm;
                    interProcCommProbability--;
                } else if(critSecProbability>0){
                    probabilityArray[count] = critSec;
                    critSecProbability--;
                }

                count--;
            }

            int numberOfInstructions = generate.nextInt(15)+15;
            int numberOfCycles;
            int insureCrit = generate.nextInt(numberOfInstructions);

            for(int i=0; i<numberOfInstructions; i++){

                int selection = generate.nextInt(10);

                switch(probabilityArray[selection]) {
                    case 1:
                        numberOfCycles = generate.nextInt(25) + 25;
                        textBlock.add("CALCULATE." + numberOfCycles + ".");
                        break;
                    case 2:
                        numberOfCycles = generate.nextInt(15) + 15;
                        textBlock.add("I/O." + numberOfCycles + ".");
                        break;
                    case 3:
                        textBlock.add("YIELD.");
                        break;
                    case 5:
                        textBlock.add("PROC-COMM.");
                        break;
                    case 6:
                        numberOfCycles = generate.nextInt(15) + 15;
                        textBlock.add("CRITICAL." + numberOfCycles + ".");
                        break;
                }

                //Makes each process have a critical section
                if(i==insureCrit){
                    numberOfCycles = generate.nextInt(15) + 15;
                    textBlock.add("CRITICAL." + numberOfCycles + ".");
                }

                if(generate.nextInt(200) < childProbability)
                    textBlock.add("SPAWN.");

            }

            priority = generate.nextInt(200)+priority;
            memoryRequirement = generate.nextInt(150)+memoryRequirement;
            int r1Req = generate.nextInt(150)+memoryRequirement;
            int r2Req = generate.nextInt(150)+memoryRequirement;

            result.getPcb().setJobType(jobType);
            result.getPcb().setMemoryReq(memoryRequirement);
            result.getPcb().setPriority(priority);
            result.getPcb().setProcessState(PCB.ProcessState.NEW);
            result.getPcb().setProgramCounter(0);
            result.getPcb().setTotalCpuCyclesUsed(0);
            result.getPcb().setInstructions(textBlock);
            result.getPcb().generateAndSetPid();
            result.getPcb().generateAndSetLogicalMemory();
            result.getPcb().setResourceOneReq(r1Req);
            result.getPcb().setResourceTwoReq(r2Req);

            //positive = pages per instruction : negative = instructions per page
            if(result.getPcb().getMemoryReq() >= result.getPcb().getInstructions().size()){
                if(result.getPcb().getMemoryReq()%result.getPcb().getInstructions().size() != 0)
                    pageLoadIter = result.getPcb().getMemoryReq()/result.getPcb().getInstructions().size() + 1;
                else
                    pageLoadIter = result.getPcb().getMemoryReq()/result.getPcb().getInstructions().size();

                result.getPcb().setNextLoadAmnt(pageLoadIter);
            } else {
                    pageLoadIter = -1*result.getPcb().getInstructions().size()/result.getPcb().getMemoryReq();
                    result.getPcb().setNextLoadAmnt(1);
            }

            result.getPcb().setPageLoadIncr(pageLoadIter);
            result.getPcb().setOffset(result.getPcb().getNextLoadAmnt());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
