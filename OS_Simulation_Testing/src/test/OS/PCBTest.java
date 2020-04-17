package test.OS; 

import OS.PCB;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.ArrayList;

import static org.junit.Assert.*;

/** 
* PCB Tester. 
* 
* @author <Authors name> 
* @since <pre>Apr 15, 2020</pre> 
* @version 1.0 
*/ 
public class PCBTest {

@Before
public void before() throws Exception {
    PCB.setBaseRegBuilder(0);
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: getBaseRegBuilder() 
* 
*/ 
@Test
public void testGetBaseRegBuilder() throws Exception { 
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getBaseRegBuilder());
}

/** 
* 
* Method: setBaseRegBuilder(int baseRegBuilder) 
* 
*/ 
@Test
public void testSetBaseRegBuilder() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setBaseRegBuilder(5);
    assertEquals(5, pcb1.getBaseRegBuilder());
}

/** 
* 
* Method: getPid() 
* 
*/ 
@Test
public void testGetPid() throws Exception { 
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getPid());
}

/** 
* 
* Method: setPid(int pid) 
* 
*/ 
@Test
public void testSetPid() throws Exception {
    PCB pcb1 = new PCB();
    int pid = 101010;
    pcb1.setPid(pid);
    assertEquals(pid, pcb1.getPid());
}

/** 
* 
* Method: getProgramCounter() 
* 
*/ 
@Test
public void testGetProgramCounter() throws Exception { 
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getProgramCounter());
}

/** 
* 
* Method: setProgramCounter(int programCounter) 
* 
*/ 
@Test
public void testSetProgramCounter() throws Exception { 
    PCB pcb1 = new PCB();
    pcb1.setProgramCounter(10);
    assertEquals(10, pcb1.getProgramCounter());
}

/** 
* 
* Method: getTotalCpuCyclesUsed() 
* 
*/ 
@Test
public void testGetTotalCpuCyclesUsed() throws Exception { 
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getTotalCpuCyclesUsed());
} 

/** 
* 
* Method: setTotalCpuCyclesUsed(int totalCpuCyclesUsed) 
* 
*/ 
@Test
public void testSetTotalCpuCyclesUsed() throws Exception { 
    PCB pcb1 = new PCB();
    pcb1.setTotalCpuCyclesUsed(100);
    assertEquals(100, pcb1.getTotalCpuCyclesUsed());
}

/** 
* 
* Method: getPriority() 
* 
*/ 
@Test
public void testGetPriority() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(100, pcb1.getPriority());
}

/** 
* 
* Method: setPriority(int priority) 
* 
*/ 
@Test
public void testSetPriority() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setPriority(20);
    assertEquals(20, pcb1.getPriority());
}

/** 
* 
* Method: getJobType() 
* 
*/ 
@Test
public void testGetJobType() throws Exception { 
    PCB pcb1 = new PCB();
    assertEquals("", pcb1.getJobType());
}

/** 
* 
* Method: setJobType(String jobType) 
* 
*/ 
@Test
public void testSetJobType() throws Exception { 
    PCB pcb1 = new PCB();
    pcb1.setJobType("Web Browser");
    assertEquals("Web Browser", pcb1.getJobType());
}

/** 
* 
* Method: getInstructions() 
* 
*/ 
@Test
public void testGetInstructions() throws Exception { 
    PCB pcb1 = new PCB();
    ArrayList<String> actual = new ArrayList<>();
    assertEquals(actual, pcb1.getInstructions());
}

/** 
* 
* Method: setInstructions(ArrayList<String> instructions) 
* 
*/ 
@Test
public void testSetInstructions() throws Exception {
    PCB pcb1 = new PCB();
    ArrayList<String> actual = new ArrayList<>();
    actual.add("CALCULATE.50.");
    actual.add("CALCULATE.25.");
    actual.add("CALCULATE.17.");
    pcb1.setInstructions(actual);
    assertEquals(actual, pcb1.getInstructions());
}

/** 
* 
* Method: getBaseRegister() 
* 
*/ 
@Test
public void testGetBaseRegister() throws Exception { 
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getBaseRegister());
}

/** 
* 
* Method: setBaseRegister(int baseRegister) 
* 
*/ 
@Test
public void testSetBaseRegister() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setBaseRegister(10);
    assertEquals(10, pcb1.getBaseRegister());
} 

/** 
* 
* Method: getLimitRegister() 
* 
*/ 
@Test
public void testGetLimitRegister() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getLimitRegister());
}

/** 
* 
* Method: setLimitRegister(int limitRegister) 
* 
*/ 
@Test
public void testSetLimitRegister() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setLimitRegister(10);
    assertEquals(10, pcb1.getLimitRegister());
}

/** 
* 
* Method: setPageCtr(int pageCtr) 
* 
*/ 
@Test
public void testSetPageCtr() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setPageCtr(5);
    assertEquals(5, pcb1.getPageCtr());
} 

/** 
* 
* Method: getPageCtr() 
* 
*/ 
@Test
public void testGetPageCtr() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getPageCtr());
} 

/** 
* 
* Method: getNumOfPages() 
* 
*/ 
@Test
public void testGetNumOfPages() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getNumOfPages());
}

/** 
* 
* Method: setNumOfPages(int numOfPages) 
* 
*/ 
@Test
public void testSetNumOfPages() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setNumOfPages(10);
    assertEquals(10, pcb1.getNumOfPages());
}

/** 
* 
* Method: getLastCpuIter() 
* 
*/ 
@Test
public void testGetLastCpuIter() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getLastCpuIter());
}

/** 
* 
* Method: setLastCpuIter(int lastCpuIter) 
* 
*/ 
@Test
public void testSetLastCpuIter() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setLastCpuIter(10);
    assertEquals(10, pcb1.getLastCpuIter());
}

/** 
* 
* Method: getPageLoadIncr() 
* 
*/ 
@Test
public void testGetPageLoadIncr() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getPageLoadIncr());
} 

/** 
* 
* Method: setPageLoadIncr(int pageLoadIncr) 
* 
*/ 
@Test
public void testSetPageLoadIncr() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setPageLoadIncr(10);
    assertEquals(10, pcb1.getPageLoadIncr());
}

/** 
* 
* Method: getNextLoadAmnt() 
* 
*/ 
@Test
public void testGetNextLoadAmnt() throws Exception { 
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getNextLoadAmnt());
}

/** 
* 
* Method: setNextLoadAmnt(int nextLoadAmnt) 
* 
*/ 
@Test
public void testSetNextLoadAmnt() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setNextLoadAmnt(10);
    assertEquals(10, pcb1.getNextLoadAmnt());
}

/** 
* 
* Method: getOffset() 
* 
*/ 
@Test
public void testGetOffset() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(1, pcb1.getOffset());
}

/** 
* 
* Method: setOffset(int offset) 
* 
*/ 
@Test
public void testSetOffset() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setOffset(5);
    assertEquals(5, pcb1.getOffset());
}

/** 
* 
* Method: getMemoryReq() 
* 
*/ 
@Test
public void testGetMemoryReq() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getMemoryReq());
}

/** 
* 
* Method: setMemoryReq(int memoryReq) 
* 
*/ 
@Test
public void testSetMemoryReq() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setMemoryReq(10);
    assertEquals(10, pcb1.getMemoryReq());
}

/** 
* 
* Method: getResourceOneReq() 
* 
*/ 
@Test
public void testGetResourceOneReq() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getResourceOneReq());
}

/** 
* 
* Method: setResourceOneReq(int resourceOneReq) 
* 
*/ 
@Test
public void testSetResourceOneReq() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setResourceOneReq(5);
    assertEquals(5, pcb1.getResourceOneReq());
}

/** 
* 
* Method: getResourceTwoReq() 
* 
*/ 
@Test
public void testGetResourceTwoReq() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getResourceTwoReq());
}

/** 
* 
* Method: setResourceTwoReq(int resourceTwoReq) 
* 
*/ 
@Test
public void testSetResourceTwoReq() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setResourceTwoReq(5);
    assertEquals(5, pcb1.getResourceTwoReq());
}

/** 
* 
* Method: getMailKey() 
* 
*/ 
@Test
public void testGetMailKey() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(0, pcb1.getMailKey());
}

/** 
* 
* Method: setMailKey(int mailKey) 
* 
*/ 
@Test
public void testSetMailKey() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setMailKey(10);
    assertEquals(10, pcb1.getMailKey());
}

/** 
* 
* Method: getMailData() 
* 
*/ 
@Test
public void testGetMailData() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals("", pcb1.getMailData());
}

/** 
* 
* Method: setMailData(String mailData) 
* 
*/ 
@Test
public void testSetMailData() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setMailData("Data");
    assertEquals("Data", pcb1.getMailData());
}

/** 
* 
* Method: hasProcessResources() 
* 
*/ 
@Test
public void testHasProcessResources() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(false, pcb1.hasProcessResources());
}

/** 
* 
* Method: setHasProcessResources(boolean hasProcessResources) 
* 
*/ 
@Test
public void testSetHasProcessResources() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setHasProcessResources(true);
    assertEquals(true, pcb1.hasProcessResources());
}

/** 
* 
* Method: hasMemory() 
* 
*/ 
@Test
public void testHasMemory() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(false, pcb1.hasMemory());
}

/** 
* 
* Method: setHasMemory(boolean hasMemory) 
* 
*/ 
@Test
public void testSetHasMemory() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setHasMemory(true);
    assertEquals(true, pcb1.hasMemory());
}

/** 
* 
* Method: isInRamOnly() 
* 
*/ 
@Test
public void testIsInRamOnly() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(false, pcb1.isInRamOnly());
}

/** 
* 
* Method: setInRamOnly(boolean inRamOnly) 
* 
*/ 
@Test
public void testSetInRamOnly() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setInRamOnly(true);
    assertEquals(true, pcb1.isInRamOnly());
}

/** 
* 
* Method: isPageLoadNeeded() 
* 
*/ 
@Test
public void testIsPageLoadNeeded() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(false, pcb1.isPageLoadNeeded());
}

/** 
* 
* Method: setPageLoadNeeded(boolean pageLoadNeeded) 
* 
*/ 
@Test
public void testSetPageLoadNeeded() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setPageLoadNeeded(true);
    assertEquals(true, pcb1.isPageLoadNeeded());
}

/** 
* 
* Method: getProcessState() 
* 
*/ 
@Test
public void testGetProcessState() throws Exception {
    PCB pcb1 = new PCB();
    assertEquals(PCB.ProcessState.NEW, pcb1.getProcessState());
}

/** 
* 
* Method: setProcessState(ProcessState processState) 
* 
*/ 
@Test
public void testSetProcessState() throws Exception {
    PCB pcb1 = new PCB();
    pcb1.setProcessState(PCB.ProcessState.READY);
    assertEquals(PCB.ProcessState.READY, pcb1.getProcessState());
}

/** 
* 
* Method: generateAndSetPid() 
* 
*/ 
@Test
public void testGenerateAndSetPid() throws Exception { 
    PCB pcb1 = new PCB();
    pcb1.generateAndSetPid();
    assertNotEquals(0, pcb1.getPid());

    boolean b1 = (100000 <= pcb1.getPid());
    boolean b2 = (899999 + 100000 > pcb1.getPid());
    assertTrue(b1);
    assertTrue(b2);
} 

/** 
* 
* Method: generateAndSetLogicalMemory() 
* 
*/ 
@Test
public void testGenerateAndSetLogicalMemory() throws Exception {
    PCB pcb1 = new PCB();
    PCB.setBaseRegBuilder(5);
    pcb1.setMemoryReq(5);
    pcb1.generateAndSetLogicalMemory();

    assertEquals(5, pcb1.getBaseRegister());
    assertEquals(5, pcb1.getLimitRegister());
    assertEquals(10, PCB.getBaseRegBuilder());
    assertEquals(5, pcb1.getPageCtr());
}

/** 
* 
* Method: shiftToNextInstruction() 
* 
*/ 
@Test
public void testShiftToNextInstruction() throws Exception {
    PCB pcb1 = new PCB();
    ArrayList<String> actual = new ArrayList<>();
    actual.add("CALCULATE.50.");
    actual.add("CALCULATE.25.");
    actual.add("CALCULATE.17.");
    pcb1.setInstructions(actual);

    assertEquals(0, pcb1.getProgramCounter());
    assertEquals(actual, pcb1.getInstructions());

    pcb1.shiftToNextInstruction();

    assertEquals("FINISHED.", pcb1.getInstructions().get(0));
    assertEquals(1, pcb1.getProgramCounter());
}

@Test
public void testToString() throws Exception {
    PCB pcb1 = new PCB();
    assertNotNull(pcb1.toString());
}
} 
