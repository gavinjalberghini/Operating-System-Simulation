package test.OS; 

import OS.Frame;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/** 
* Frame Tester. 
* 
* @author <Gavin Alberghini>
* @since <pre>Apr 14, 2020</pre> 
* @version 1.0 
*/ 
public class FrameTest { 

@Before
public void before() throws Exception {
    Frame.setAddressBuilder(1);
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: setPhysicalAddress(int physicalAddress) 
* 
*/ 
@Test
public void testSetPhysicalAddress() throws Exception {
    Frame f1 = new Frame();
    f1.setPhysicalAddress(5);
    assertEquals(5, f1.getPhysicalAddress());
} 

/** 
* 
* Method: getPhysicalAddress() 
* 
*/ 
@Test
public void testGetPhysicalAddress() throws Exception {
    Frame f1 = new Frame();
    assertEquals(1, f1.getPhysicalAddress());

    Frame f2 = new Frame();
    assertEquals(2, f2.getPhysicalAddress());

    Frame f3 = new Frame();
    assertEquals(3, f3.getPhysicalAddress());
} 

/** 
* 
* Method: getSize() 
* 
*/ 
@Test
public void testGetSize() throws Exception { 
    Frame f1 = new Frame();
    assertEquals(1, f1.getSize());
} 

/** 
* 
* Method: setFree(boolean free) 
* 
*/ 
@Test
public void testSetFree() throws Exception { 
    Frame f1 = new Frame();
    f1.setFree(false);
    assertEquals(false, f1.isFree());
} 

/** 
* 
* Method: isFree() 
* 
*/ 
@Test
public void testIsFree() throws Exception { 
    Frame f1 = new Frame();
    assertEquals(true, f1.isFree());
} 

/** 
* 
* Method: occupy(int pid) 
* 
*/ 
@Test
public void testOccupy() throws Exception { 
    int pid = 16472812;
    Frame f1 = new Frame();
    f1.occupy(pid);
    assertEquals(false, f1.isFree());
    assertEquals(pid, f1.getProccessID());
} 

/** 
* 
* Method: free() 
* 
*/ 
@Test
public void testFree() throws Exception {
    int pid = 16472812;
    Frame f1 = new Frame();
    f1.occupy(pid);
    f1.free();
    assertEquals(true, f1.isFree());
    assertEquals(-1, f1.getProccessID());
} 

/** 
* 
* Method: setVMFrame(boolean VMFrame) 
* 
*/ 
@Test
public void testSetVMFrame() throws Exception { 
    Frame f1 = new Frame();
    f1.setVMFrame(true);
    assertEquals(true, f1.isVMFrame());
}

/** 
* 
* Method: isVMFrame() 
* 
*/ 
@Test
public void testIsVMFrame() throws Exception {
    Frame f1 = new Frame();
    assertEquals(false, f1.isVMFrame());
} 

/** 
* 
* Method: getProccessID() 
* 
*/ 
@Test
public void testGetProccessID() throws Exception {
    int pid = 16472812;
    Frame f1 = new Frame();
    f1.occupy(pid);
    assertEquals(pid, f1.getProccessID());
} 

/** 
* 
* Method: setProccessID(int proccessID) 
* 
*/ 
@Test
public void testSetProccessID() throws Exception {
    int pid = 16472812;
    Frame f1 = new Frame();
    f1.setProccessID(pid);
    assertEquals(pid, f1.getProccessID());
}

/** 
* 
* Method: getAddressBuilder() 
* 
*/ 
@Test
public void testGetAddressBuilder() throws Exception {
    assertEquals(1, Frame.getAddressBuilder());
}

/** 
* 
* Method: setAddressBuilder(int addressBuilder) 
* 
*/ 
@Test
public void testSetAddressBuilder() throws Exception { 
    Frame.setAddressBuilder(10);
    assertEquals(10, Frame.getAddressBuilder());
}

/** 
* 
* Method: generateAndSetPhysicalAddress() 
* 
*/ 
@Test
public void testGenerateAndSetPhysicalAddress() throws Exception { 
    Frame f1 = new Frame();
    assertEquals(Frame.getAddressBuilder()-1, f1.getPhysicalAddress());
    assertEquals(Frame.getAddressBuilder(), f1.getPhysicalAddress()+1);
}

@Test
public void testToString() throws Exception{
    Frame f1 = new Frame();

    String result = "";

    result += "Physical Addr: " + f1.getPhysicalAddress() + "\n";
    result += "Contents: " + f1.getProccessID();

    assertNotNull(f1.toString());
    assertEquals(result, f1.toString());
}
} 
