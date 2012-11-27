package org.hacklace.animator;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class IniConfigurationTest extends TestCase {
	
	private IniConf conf;
	
	protected void setUp() {
		conf = new IniConf(); 
	}
	
	protected void tearDown() {
		conf = null;
	}
	
	public void testGridConf() {
		assertEquals(5, conf.rows());
		assertEquals(7, conf.columns());
	}
	
	public void testDisplayConf() {
		assertEquals(500, conf.displayHeight());
		assertEquals(700, conf.displayWidth());
	}
	
	public void testFlashConf() {
		assertEquals("/sys/class/tty", conf.device());
	}
	
	public void testAnimationConf() {
		List<Integer> delayList = Arrays.asList(0, 1, 2, 3, 5, 8, 13, 21);
		List<Integer> speedList = Arrays.asList(50, 30, 18, 11, 7, 5, 3, 2);
		assertEquals(speedList, conf.speedList());
		assertEquals(delayList, conf.delayList());
	}
	

}