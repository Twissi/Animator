package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.displaybuffer.Grid;
import org.hacklace.animator.gui.FontUtil;

public class GridTest extends TestCase {
	
	public void testGrid() {
		Grid g = new Grid(7, 5);
		
		boolean[][] data = g.getData();
		
		data[4][2] = true;
		data[1][3] = true;
		data[3][6] = true;	
		
		assertTrue(data[4][2]);
		assertTrue(data[1][3]);
		assertTrue(data[3][6]);	
	}

	public void testFontUtil() {
		Grid g = new Grid(7,5);
		g.setDataFromBytes( FontUtil.repr(67));
		System.out.println(g);
		assertFalse(g.data[0][6]);
		assertTrue(g.data[0][5]);
	}
}
