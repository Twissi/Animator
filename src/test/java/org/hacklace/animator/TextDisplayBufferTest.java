package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.displaybuffer.Grid;

public class TextDisplayBufferTest extends TestCase {
	
	private Grid g1;
	private Grid g2;
	
	protected void setUp() {
		g1 = new Grid();
		g2 = new Grid();
		
		boolean[][] data;
		
		data = g1.getData();
		
		data[4][2] = true;
		data[1][3] = true;
		data[3][6] = true;
		data[3][1] = true;
	}
	
	protected void tearDown() {
		g1 = null;
		g2 = null;
	}
	
	public void testCurrent() {
		
	}
	
	public void testPrevious() {
		
	}
	
	public void testNext() {
		
	}
}
