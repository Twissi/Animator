package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.displaybuffer.Grid;

public class GridTest extends TestCase {
	
	public void testGrid() {
		Grid g = new Grid(7, 5);
		
		boolean[][] data = g.getData();
		
		data[4][2] = true;
		data[1][3] = true;
		data[3][6] = true;
		data[4][2] = true;		
		
		assertTrue(data[4][2]);
		assertTrue(data[1][3]);
		assertTrue(data[3][6]);
		assertTrue(data[4][2]);		
	}

}
