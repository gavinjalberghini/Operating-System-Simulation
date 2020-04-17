package test.OS; 

import OS.Frame;
import OS.MMU;
import OS.Memory;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/** 
* MMU Tester. 
* 
* @author <Authors name> 
* @since <pre>Apr 14, 2020</pre> 
* @version 1.0 
*/ 
public class MMUTest { 

@Before
public void before() throws Exception {
    Frame.setAddressBuilder(1);
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: getMemoryMap() 
* 
*/ 
@Test
public void testGetMemoryMap() throws Exception {
    MMU mmu1 = new MMU(2, 2);
    assertNotNull(mmu1.getMemoryMap());
} 

/** 
* 
* Method: setMemoryMap(HashMap<Integer, Integer> memoryMap) 
* 
*/ 
@Test
public void testSetMemoryMap() throws Exception { 
    MMU mmu1 = new MMU(2, 2);
    int log = 0;
    int phy = 1;

    HashMap<Integer, Integer> memMap = new HashMap<>();
    memMap.put(log, phy);

    mmu1.setMemoryMap(memMap);
    assertEquals(memMap, mmu1.getMemoryMap());
}

/** 
* 
* Method: updateMemoryMap(int logical, int physical) 
* 
*/ 
@Test
public void testUpdateMemoryMap() throws Exception {
    MMU mmu1 = new MMU(2, 2);
    int log = 0;
    int phy = 1;

    mmu1.updateMemoryMap(log, phy);
    assertEquals(phy, mmu1.getMemoryMap().get(log).intValue());
}

/** 
* 
* Method: getCache() 
* 
*/ 
@Test
public void testGetCache() throws Exception {
    MMU mmu1 = new MMU(2, 2);
    assertNotNull(mmu1.getCache());
}

/** 
* 
* Method: setCache(HashMap<Integer, Integer> cache) 
* 
*/ 
@Test
public void testSetCache() throws Exception {
    MMU mmu1 = new MMU(2, 2);
    int log = 0;
    int phy = 1;

    HashMap<Integer, Integer> cache = new HashMap<>();
    cache.put(log, phy);

    mmu1.setCache(cache);
    assertEquals(cache, mmu1.getCache());
}

/** 
* 
* Method: updateCache(int logical, int physical) 
* 
*/ 
@Test
public void testUpdateCache() throws Exception {
    MMU mmu1 = new MMU(2, 2);
    int log = 0;
    int phy = 1;

    mmu1.updateCache(log, phy);
    assertEquals(phy, mmu1.getCache().get(log).intValue());
}

/** 
* 
* Method: getRam() 
* 
*/ 
@Test
public void testGetRam() throws Exception { 
    MMU mmu1 = new MMU(5, 10);

    assertNotNull(mmu1.getRam());
    assertEquals(5, mmu1.getRam().getSize());
}

/** 
* 
* Method: getVirtualMemory() 
* 
*/ 
@Test
public void testGetVirtualMemory() throws Exception {
    MMU mmu1 = new MMU(5, 10);

    assertNotNull(mmu1.getVirtualMemory());
    assertEquals(10, mmu1.getVirtualMemory().getSize());
}

/** 
* 
* Method: setRam(Memory ram) 
* 
*/ 
@Test
public void testSetRam() throws Exception {
    Memory m1 = new Memory(false, 10);
    MMU mmu1 = new MMU(20, 30);
    mmu1.setRam(m1);
    assertEquals(m1, mmu1.getRam());
}

/** 
* 
* Method: setVirtualMemory(Memory virtualMemory) 
* 
*/ 
@Test
public void testSetVirtualMemory() throws Exception {
    Memory m1 = new Memory(false, 10);
    MMU mmu1 = new MMU(20, 30);
    mmu1.setVirtualMemory(m1);
    assertEquals(m1, mmu1.getVirtualMemory());
}

/** 
* 
* Method: getResourceOne() 
* 
*/ 
@Test
public void testGetResourceOne() throws Exception { 
    MMU mmu1 = new MMU(5, 5);
    assertEquals(6000, mmu1.getResourceOne());
}

/** 
* 
* Method: getResourceTwo() 
* 
*/ 
@Test
public void testGetResourceTwo() throws Exception {
    MMU mmu1 = new MMU(5, 5);
    assertEquals(6000, mmu1.getResourceTwo());
}

/** 
* 
* Method: setResourceOne(int resourceOne) 
* 
*/ 
@Test
public void testSetResourceOne() throws Exception {
    MMU mmu1 = new MMU(5, 5);
    mmu1.setResourceOne(500);
    assertEquals(500, mmu1.getResourceOne());
} 

/** 
* 
* Method: setResourceTwo(int resourceTwo) 
* 
*/ 
@Test
public void testSetResourceTwo() throws Exception {
    MMU mmu1 = new MMU(5, 5);
    mmu1.setResourceTwo(500);
    assertEquals(500, mmu1.getResourceTwo());
}

/** 
* 
* Method: toString() 
* 
*/ 
@Test
public void testToString() throws Exception { 
    MMU mmu1 = new MMU(2, 2);
    assertNotNull(mmu1.toString());
}


} 
