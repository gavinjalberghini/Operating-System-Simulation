package test.OS; 

import OS.Frame;
import OS.Memory;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Stack;

import static org.junit.Assert.*;

/** 
* Memory Tester. 
* 
* @author <Authors name> 
* @since <pre>Apr 14, 2020</pre> 
* @version 1.0 
*/ 
public class MemoryTest { 

@Before
public void before() throws Exception {
    Frame.setAddressBuilder(1);
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: setFrames(HashMap<Integer, Frame> frames) 
* 
*/ 
@Test
public void testSetFrames() throws Exception {
    Memory m1 = new Memory(false, 0);
    HashMap<Integer, Frame> frames = new HashMap<Integer, Frame>();
    Frame f1 = new Frame();
    Frame f2 = new Frame();
    frames.put(f1.getPhysicalAddress(), f1);
    frames.put(f2.getPhysicalAddress(), f2);
    m1.setFrames(frames);

    assertEquals(f1, m1.getFrames().get(f1.getPhysicalAddress()));
    assertEquals(f2, m1.getFrames().get(f2.getPhysicalAddress()));
} 

/** 
* 
* Method: getFrames() 
* 
*/ 
@Test
public void testGetFrames() throws Exception { 
    Memory m1 = new Memory(false, 2);
    assertEquals(1, m1.getFrames().get(1).getPhysicalAddress());
    assertEquals(2,  m1.getFrames().get(2).getPhysicalAddress());
}

/** 
* 
* Method: getSize() 
* 
*/ 
@Test
public void testGetSize() throws Exception { 
    Memory m1 = new Memory(false, 10);
    assertEquals(10, m1.getSize());
} 

/** 
* 
* Method: setFreeMemory(boolean freeMemory) 
* 
*/ 
@Test
public void testSetFreeMemory() throws Exception { 
    Memory m1 = new Memory(false, 10);
    m1.setFreeMemory(false);
    assertEquals(false, m1.isFreeMemory());
} 

/** 
* 
* Method: isFreeMemory() 
* 
*/ 
@Test
public void testIsFreeMemory() throws Exception {
    Memory m1 = new Memory(false, 10);
    assertEquals(true, m1.isFreeMemory());
}

/** 
* 
* Method: getNumOfFreeFrames() 
* 
*/ 
@Test
public void testGetNumOfFreeFrames() throws Exception { 
    Memory m1 = new Memory(false, 10);
    assertEquals(10, m1.getNumOfFreeFrames());
}

/** 
* 
* Method: setNumOfFreeFrames(int numOfFreeFrames) 
* 
*/ 
@Test
public void testSetNumOfFreeFrames() throws Exception { 
    Memory m1 = new Memory(false, 5);
    m1.setNumOfFreeFrames(3);
    assertEquals(3, m1.getNumOfFreeFrames());
} 

/** 
* 
* Method: getFreeFrames() 
* 
*/ 
@Test
public void testGetFreeFrames() throws Exception { 
    Memory m1 = new Memory(false, 3);
    Stack<Frame> freeFrames = new Stack<>();
    Frame.setAddressBuilder(1);
    Frame f1 = new Frame();
    Frame f2 = new Frame();
    Frame f3 = new Frame();
    freeFrames.push(f1);
    freeFrames.push(f2);
    freeFrames.push(f3);

    assertEquals(freeFrames.pop().getPhysicalAddress(), m1.getFreeFrames().pop().getPhysicalAddress());
    assertEquals(freeFrames.pop().getPhysicalAddress(), m1.getFreeFrames().pop().getPhysicalAddress());
    assertEquals(freeFrames.pop().getPhysicalAddress(), m1.getFreeFrames().pop().getPhysicalAddress());
} 

/** 
* 
* Method: setFreeFrames(Stack<Frame> freeFrames) 
* 
*/ 
@Test
public void testSetFreeFrames() throws Exception { 
    Memory m1 = new Memory(false, 2);
    Stack<Frame> freeFrames = new Stack<>();
    Frame f1 = new Frame();
    Frame f2 = new Frame();
    Frame f3 = new Frame();
    freeFrames.push(f1);
    freeFrames.push(f2);
    freeFrames.push(f3);
    m1.setFreeFrames(freeFrames);

    assertEquals(5, m1.getFreeFrames().pop().getPhysicalAddress());
    assertEquals(4, m1.getFreeFrames().pop().getPhysicalAddress());
    assertEquals(3, m1.getFreeFrames().pop().getPhysicalAddress());
}

/** 
* 
* Method: addToFrames(Frame frame) 
* 
*/ 
@Test
public void testAddToFrames() throws Exception { 
    Memory m1 = new Memory(false, 2);
    Frame f3 = new Frame();
    m1.addToFrames(f3);
    assertEquals(f3, m1.getFrames().get(f3.getPhysicalAddress()));
}

/** 
* 
* Method: removeFromFrames(Frame frame) 
* 
*/ 
@Test
public void testRemoveFromFrames() throws Exception {
    Memory m1 = new Memory(false, 2);
    Frame f3 = new Frame();
    m1.addToFrames(f3);
    assertEquals(f3, m1.getFrames().get(f3.getPhysicalAddress()));
}

/** 
* 
* Method: updateFrame(Frame frame) 
* 
*/ 
@Test
public void testUpdateFrame() throws Exception { 
    Memory m1 = new Memory(true, 2);
    Frame f1 = m1.getFrames().get(1);
    f1.occupy(101010);
    f1.setFree(true);
    f1.setVMFrame(false);
    m1.updateFrame(f1);
    assertEquals(101010, m1.getFrames().get(f1.getPhysicalAddress()).getProccessID());
    assertEquals(false, m1.getFrames().get(f1.getPhysicalAddress()).isVMFrame());
    assertEquals(true, m1.getFrames().get(f1.getPhysicalAddress()).isFree());
}

/** 
* 
* Method: addToFreeFrames(Frame frame) 
* 
*/ 
@Test
public void testAddToFreeFrames() throws Exception { 
    Memory m1 = new Memory(false, 2);
    Frame f3 = new Frame();
    m1.addToFreeFrames(f3);
    assertEquals(f3, m1.getFreeFrames().pop());
} 

/** 
* 
* Method: removeFromStack(Frame frame) 
* 
*/ 
@Test
public void testRemoveFromStack() throws Exception { 
    Memory m1 = new Memory(false, 2);
    Frame f3 = new Frame();
    m1.addToFreeFrames(f3);
    m1.removeFromStack(f3);
    assertEquals(2, m1.getFreeFrames().size());
    assertEquals(2, m1.getFreeFrames().pop().getPhysicalAddress());
    assertEquals(1, m1.getFreeFrames().pop().getPhysicalAddress());
} 

/** 
* 
* Method: getNextFreeFrame() 
* 
*/ 
@Test
public void testGetNextFreeFrame() throws Exception { 
    Memory m1 = new Memory(false, 2);
    Frame f3 = new Frame();
    m1.addToFrames(f3);
    m1.addToFreeFrames(f3);
    assertEquals(f3, m1.getNextFreeFrame());
}

/** 
* 
* Method: allocateFrame(int pid) 
* 
*/ 
@Test
public void testAllocateFrame() throws Exception { 
    Memory m1 = new Memory(false, 2);
    int pid = 101010;
    Frame prev = new Frame();
    prev.setFree(m1.getFrames().get(2).isFree());
    prev.setVMFrame(m1.getFrames().get(2).isVMFrame());
    Frame f = m1.allocateFrame(pid);
    assertEquals(pid, m1.getFrames().get(2).getProccessID());
    assertEquals(false, m1.getFrames().get(2).isFree());
    assertNotEquals(prev.isFree(), m1.getFrames().get(2).isFree());
    assertNotNull(f);
}

/** 
* 
* Method: updateNumOfFreeFrames() 
* 
*/ 
@Test
public void testUpdateNumOfFreeFrames() throws Exception { 
    Memory m1 = new Memory(false, 10);
    int[] pids = {101010, 122331, 233442, 333222, 888777};

    for(int p : pids){
        m1.allocateFrame(p);
    }

    assertEquals(5, m1.getNumOfFreeFrames());

    Frame f11 = new Frame();
    m1.addToFreeFrames(f11);
    m1.updateNumOfFreeFrames();

    assertEquals(6, m1.getNumOfFreeFrames());
} 

/** 
* 
* Method: deallocateFrame(int physicalAddr) 
* 
*/ 
@Test
public void testDeallocateFrame() throws Exception {
    Memory m1 = new Memory(false, 5);
    int[] pids = {101010, 122331, 233442, 333222, 888777};
    int[] physicalAddresses = {1, 2, 3, 4, 5};

    for(int p : pids){
        Frame f = m1.allocateFrame(p);
        assertEquals(f.isFree(), m1.getFrames().get(f.getPhysicalAddress()).isFree());
    }

    assertEquals(0, m1.getNumOfFreeFrames());

    for(int phy_id : physicalAddresses){
        Frame prev = new Frame();
        prev.setFree(m1.getFrames().get(phy_id).isFree());
        prev.setVMFrame(m1.getFrames().get(phy_id).isVMFrame());

        m1.deallocateFrame(phy_id);
        Frame f = m1.getFrames().get(phy_id);

        assertEquals(true, m1.getFrames().get(phy_id).isFree());
        assertNotEquals(prev.isFree(), m1.getFrames().get(phy_id).isFree());
        assertNotNull(f);
    }

    assertEquals(5, m1.getNumOfFreeFrames());

}

@Test
public void testInitializeMemory() throws Exception{
    Memory m1 = new Memory(false, 1);
    assertFalse(m1.getFrames().get(1).isVMFrame());

    Frame.setAddressBuilder(1);
    Memory m2 = new Memory(true, 1);
    assertTrue(m2.getFrames().get(1).isVMFrame());
}

@Test
public void testToString() throws Exception{
    Memory m1 = new Memory(false, 9);

    String result = "";

    int count = 1;
    for(Frame f : m1.getFrames().values()){
        if(count%4==0)
            result += "Frame " + f.getPhysicalAddress() + " occupied by " + f.getProccessID() + "\n";
        else
            result += "Frame " + f.getPhysicalAddress() + " occupied by " + f.getProccessID() + " | ";

        count++;
    }

    assertNotNull(m1.toString());
    assertEquals(result, m1.toString());
}

} 
