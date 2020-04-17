package test.OS; 

import OS.PDT;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.ArrayList;

import static org.junit.Assert.*;

/** 
* PDT Tester. 
* 
* @author <Authors name> 
* @since <pre>Apr 14, 2020</pre> 
* @version 1.0 
*/ 
public class PDTTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: isChild() 
* 
*/ 
@Test
public void testIsChild() throws Exception { 
    PDT pdt1 = new PDT();
    assertFalse(pdt1.isChild());
}

/** 
* 
* Method: isParent() 
* 
*/ 
@Test
public void testIsParent() throws Exception {
    PDT pdt1 = new PDT();
    assertFalse(pdt1.isParent());
}

/** 
* 
* Method: setIsChild(boolean child) 
* 
*/ 
@Test
public void testSetIsChild() throws Exception {
    PDT pdt1 = new PDT();
    pdt1.setIsChild(true);
    assertTrue(pdt1.isChild());
}

/** 
* 
* Method: setIsParent(boolean parent) 
* 
*/ 
@Test
public void testSetIsParent() throws Exception {
    PDT pdt1 = new PDT();
    pdt1.setIsParent(true);
    assertTrue(pdt1.isParent());
}

/** 
* 
* Method: getParentPid() 
* 
*/ 
@Test
public void testGetParentPid() throws Exception {
    PDT pdt1 = new PDT();
    assertEquals(-1, pdt1.getParentPid());
}

/** 
* 
* Method: setParentPid(int parentPid) 
* 
*/ 
@Test
public void testSetParentPid() throws Exception {
    PDT pdt1 = new PDT();
    int pid = 101010;
    pdt1.setParentPid(pid);
    assertEquals(pid, pdt1.getParentPid());
}

/** 
* 
* Method: addChildToPDT(int childPid) 
* 
*/ 
@Test
public void testAddChildToPDT() throws Exception {
    PDT pdt1 = new PDT();
    int childPid = 101010;
    pdt1.addChildToPDT(childPid);
    assertEquals(Integer.valueOf(childPid), pdt1.getChildrenPids().get(0));
}

/** 
* 
* Method: getChildrenPids() 
* 
*/ 
@Test
public void testGetChildrenPids() throws Exception {
    PDT pdt1 = new PDT();
    ArrayList<Integer> test = new ArrayList<>();
    assertEquals(test, pdt1.getChildrenPids());
} 

/** 
* 
* Method: removeChildFromPDT(int childPid) 
* 
*/ 
@Test
public void testRemoveChildFromPDT() throws Exception {
    PDT pdt1 = new PDT();
    int childPid = 101010;
    pdt1.addChildToPDT(childPid);
    assertEquals(Integer.valueOf(childPid), pdt1.getChildrenPids().get(0));

    pdt1.removeChildFromPDT(childPid);
    ArrayList<Integer> test = new ArrayList<>();
    assertEquals(test, pdt1.getChildrenPids());
} 


} 
