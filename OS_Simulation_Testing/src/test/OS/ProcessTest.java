package test.OS; 

import OS.PCB;
import OS.PDT;
import OS.Process;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/** 
* Process Tester. 
* 
* @author Gavin Alberghini
* @since <pre>Apr 14, 2020</pre> 
* @version 1.0 
*/ 
public class ProcessTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: getPcb() 
* 
*/ 
@Test
public void testGetPcb() throws Exception {
    Process p1 = new Process();
    assertNotNull(p1.getPcb());
}

/** 
* 
* Method: getPdt() 
* 
*/ 
@Test
public void testGetPdt() throws Exception {
    Process p1 = new Process();
    assertNotNull(p1.getPdt());
}

/** 
* 
* Method: getPipes() 
* 
*/ 
@Test
public void testGetPipes() throws Exception {
    Process p1 = new Process();
    ArrayList<Process.Pipe> pipes = new ArrayList<>();
    assertEquals(pipes, p1.getPipes());
}

/** 
* 
* Method: getPageTable() 
* 
*/ 
@Test
public void testGetPageTable() throws Exception { 
    Process p1 = new Process();
    HashMap<Integer, Integer> pageTable = new HashMap<>();
    assertEquals(pageTable, p1.getPageTable());
}

/** 
* 
* Method: getValidBits() 
* 
*/ 
@Test
public void testGetValidBits() throws Exception {
    Process p1 = new Process();
    HashMap<Integer, Integer> validBits = new HashMap<>();
    assertEquals(validBits, p1.getValidBits());
}

/** 
* 
* Method: setPcb(PCB pcb) 
* 
*/ 
@Test
public void testSetPcb() throws Exception { 
    Process p1 = new Process();
    PCB pcb1 = new PCB();
    PCB.setBaseRegBuilder(5);
    pcb1.setMemoryReq(5);
    pcb1.generateAndSetLogicalMemory();
    p1.setPcb(pcb1);
    assertEquals(pcb1, p1.getPcb());
}

/** 
* 
* Method: setPdt(PDT pdt) 
* 
*/ 
@Test
public void testSetPdt() throws Exception {
    Process p1 = new Process();
    PDT pdt1 = new PDT();
    int childPid = 101010;
    pdt1.addChildToPDT(childPid);
    p1.setPdt(pdt1);
    assertEquals(pdt1, p1.getPdt());
}

/** 
* 
* Method: addToPageTable(int logical, int physical, int validBit) 
* 
*/ 
@Test
public void testAddToPageTable() throws Exception { 
    Process p1 = new Process();
    Integer l = 0;
    Integer phy = 1;
    Integer valid = 0;
    int prgctr = p1.getPcb().getPageCtr();
    p1.addToPageTable(l, phy, valid);

    assertEquals(phy, p1.getPageTable().get(l));
    assertEquals(valid, p1.getValidBits().get(l));
    assertEquals(prgctr + 1, p1.getPcb().getPageCtr());
}

/** 
* 
* Method: buildPipe(int companionPid, int lock, int key) 
* 
*/ 
@Test
public void testBuildPipe() throws Exception {
    Process p1 = new Process();
    int p2Pid = 121212;
    int lock = 111111;
    int key = 123456;
    p1.buildPipe(p2Pid, lock, key);

    assertEquals(1, p1.getPipes().size());
    assertEquals(p2Pid, p1.getPipes().get(0).returnInfo()[0]);
    assertEquals(key, p1.getPipes().get(0).returnInfo()[1]);
    assertEquals(lock, p1.getPipes().get(0).returnInfo()[2]);
}

/** 
* 
* Method: toString() 
* 
*/ 
@Test
public void testToString() throws Exception {

    Process p1 = new Process();
    int p2Pid = 121212;
    int lock = 111111;
    int key = 123456;
    p1.buildPipe(p2Pid, lock, key);
    ArrayList<String> actual = new ArrayList<>();
    actual.add("CALCULATE.50.");
    actual.add("CALCULATE.25.");
    actual.add("CALCULATE.17.");
    actual.add("CALCULATE.1.");
    actual.add("CALCULATE.7.");
    actual.add("CALCULATE.55.");
    p1.getPcb().setInstructions(actual);

    p1.addToPageTable(0, 1, 0);
    p1.addToPageTable(1, 2, 0);
    p1.addToPageTable(2, 3, 0);
    p1.addToPageTable(3, 4, 0);
    p1.addToPageTable(4, 5, 0);
    p1.addToPageTable(5, 6, 0);

    String result = "";
    result += "STATIC INFO:\n";
    result += "Job Type -- " + p1.getPcb().getJobType() + " : ";
    result += "PID -- " + p1.getPcb().getPid() + " : ";
    result += "Memory Requirement -- " + p1.getPcb().getMemoryReq() + "\n";
    result += "Text block size -- " + p1.getPcb().getInstructions().size() + " : ";
    result += "Base register -- " + p1.getPcb().getBaseRegister() + " : ";
    result += "Limit register -- " + p1.getPcb().getLimitRegister() + "\n";
    result += "Resource one req -- " + p1.getPcb().getResourceOneReq() + " : ";
    result += "Resource two req -- " + p1.getPcb().getResourceTwoReq() + " : \n\n";
    result += "DYNAMIC INFO:\n";
    result += "Current Program Pointer -- " + p1.getPcb().getProgramCounter() + " : ";
    result += "Current Priority -- " + p1.getPcb().getPriority() + " : ";
    result += "Current Process State -- " + p1.getPcb().getProcessState() + "\n";
    result += "Has Process Resources -- " + p1.getPcb().hasProcessResources() + "\n\n";
    result += "TEXT BLOCK:\n";

    int count = 1;
    for (String s:p1.getPcb().getInstructions()) {
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
    for (Integer i:p1.getPageTable().keySet()) {
        result += i + " -- " + p1.getPageTable().get(i);

        if(count%5==0){
            result+="\n";
        } else {
            result+=" : ";
        }

        count ++;
    }

    result += "\nPIPES\n";

    for (Process.Pipe p: p1.getPipes()){
        result += "Pipe with process - " + p.returnInfo()[0] + " : Companion Message - " + p.returnInfo()[4] +
                "\n\t Personal Message - " + p.returnInfo()[3] + " : Lock - " + p.returnInfo()[2] + " : Key - " + p.returnInfo()[1] + "\n";
    }

    assertEquals(result, p1.toString());


}

/** 
* 
* Method: communicate(Process companion) 
* 
*/ 
@Test
public void testCommunicate() throws Exception { 

    Process p1 = new Process();
    Process p2 = new Process();
    int p1Pid = 101010;
    int p2Pid = 202020;
    p1.getPcb().setPid(p1Pid);
    p2.getPcb().setPid(p2Pid);

    int lock = 100;
    int key = 7;
    p1.buildPipe(p2Pid, lock, key);
    p2.buildPipe(p1Pid, key, lock);
    p2.getPipes().get(0).communicate(p1);
    assertNotEquals(0, p2.getPipes().get(0).companionMessage);

}

/** 
* 
* Method: getMessage(int key) 
* 
*/ 
@Test
public void testGetMessage() throws Exception { 
    Process p1 = new Process();
    int p2Pid = 202020;
    int lock = 100;
    int key = 7;
    p1.buildPipe(p2Pid, lock, key);

    assertEquals(-1, p1.getPipes().get(0).getMessage(key + 1));
    p1.getPipes().get(0).getMessage(lock);
    assertNotEquals(0, p1.getPipes().get(0).personalMessage);
}


/** 
* 
* Method: setPipeLock(int lock) 
* 
*/ 
@Test
public void testSetPipeLock() throws Exception {
    Process p1 = new Process();
    int p2Pid = 202020;
    int lock = 100;
    int key = 7;
    p1.buildPipe(p2Pid, lock, key);
    p1.getPipes().get(0).setPipeLock(5);
    assertEquals(5, p1.getPipes().get(0).returnInfo()[2]);
} 

/** 
* 
* Method: setPipeKey(int key) 
* 
*/ 
@Test
public void testSetPipeKey() throws Exception {
    Process p1 = new Process();
    int p2Pid = 202020;
    int lock = 100;
    int key = 7;
    p1.buildPipe(p2Pid, lock, key);
    p1.getPipes().get(0).setPipeKey(5);
    assertEquals(5, p1.getPipes().get(0).returnInfo()[1]);
} 

/** 
* 
* Method: useKey() 
* 
*/ 
@Test
public void testUseKey() throws Exception {
    Process p1 = new Process();
    int p2Pid = 202020;
    int lock = 100;
    int key = 7;
    p1.buildPipe(p2Pid, lock, key);
    assertEquals(key, p1.getPipes().get(0).useKey());
} 

/** 
* 
* Method: generateMessage() 
* 
*/ 
@Test
public void testGenerateMessage() throws Exception {
    Process p1 = new Process();
    int p2Pid = 202020;
    int lock = 100;
    int key = 7;
    p1.buildPipe(p2Pid, lock, key);

    assertEquals(-1, p1.getPipes().get(0).getMessage(key + 1));
    p1.getPipes().get(0).getMessage(lock);
    assertNotEquals(0, p1.getPipes().get(0).personalMessage);
} 

/** 
* 
* Method: setConnectedProcPid(int pid) 
* 
*/ 
@Test
public void testSetConnectedProcPid() throws Exception {
    Process p1 = new Process();
    int p2Pid = 202020;
    int lock = 100;
    int key = 7;
    p1.buildPipe(0, lock, key);
    p1.getPipes().get(0).setConnectedProcPid(p2Pid);

    assertEquals(p2Pid, p1.getPipes().get(0).returnInfo()[0]);
} 

} 
