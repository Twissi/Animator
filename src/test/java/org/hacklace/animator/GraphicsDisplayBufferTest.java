package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;
import org.hacklace.animator.displaybuffer.Grid;

public class GraphicsDisplayBufferTest extends TestCase {
	
	private Grid g0;
	private Grid g1;
	private Grid g2;
	private Grid g3;
	
	private GraphicDisplayBuffer gdb;
	
	protected void setUp() {
		g0 = new Grid(); // empty on purpose
		g1 = new Grid();
		g2 = new Grid();
		g3 = new Grid();
		
		boolean[][] data;
		
		/*
		 * First grid
		 */
		
		data = g1.getData();
		
		data[4][2] = true;
		data[1][3] = true;
		data[3][6] = true;
		data[3][1] = true;
		
		/*
		 * Second grid
		 */
		
		data = g2.getData();
		
		data[4][2] = true;
		data[1][3] = true;
		data[3][6] = true;
		data[3][1] = true;
		
		/*
		 * Third grid
		 */
		
		data = g3.getData();
		
		data[0][0] = true;
		data[0][1] = true;
		data[0][2] = true;
		data[0][3] = true;
		
		
	}
	
	protected void tearDown() {
		g0 = null;
		g1 = null;
		g2 = null;
		g3 = null;
	}

	public void testCurrent() {
		gdb = new GraphicDisplayBuffer();		
		Grid outGrid;

		gdb.addGrid(g0);		
		outGrid = gdb.getCurrent();		
		assertEquals(gdb.getStepWidth(), gdb.getPosition());
		assertEquals(g0, outGrid);
		
		gdb.addGrid(g1);
		outGrid = gdb.getCurrent();		
		assertEquals(2 * gdb.getStepWidth(), gdb.getPosition());
		assertEquals(g1, outGrid);
		
		gdb.addGrid(g2);		
		outGrid = gdb.getCurrent();		
		assertEquals(g2, outGrid);
		
		gdb.addGrid(g3);		
		outGrid = gdb.getCurrent();		
		assertEquals(g3, outGrid);		
	}
	
	public void testPrevious() {
		gdb = new GraphicDisplayBuffer();		
		Grid outGrid;

		gdb.addGrid(g0);
		assertEquals(gdb.getStepWidth(), gdb.getPosition());
		
		gdb.addGrid(g1);
		outGrid = gdb.getPrevious();
		assertEquals(2 * gdb.getStepWidth(), gdb.getPosition());
		assertEquals(g0, outGrid);
		
		gdb.addGrid(g2);		
		outGrid = gdb.getPrevious();
		assertEquals(3 * gdb.getStepWidth(), gdb.getPosition());
		assertEquals(g1, outGrid);
		
		gdb.addGrid(g3);		
		outGrid = gdb.getPrevious();
		assertEquals(4 * gdb.getStepWidth(), gdb.getPosition());
		assertEquals(g2, outGrid);
	}
	
	public void testNext() {
		gdb = new GraphicDisplayBuffer();		
		Grid outGrid;

		gdb.addGrid(g0);
		assertEquals(gdb.getStepWidth(), gdb.getPosition());
		
		gdb.addGrid(g1);
		outGrid = gdb.getPrevious();
		assertEquals(2 * gdb.getStepWidth(), gdb.getPosition());
		assertEquals(g0, outGrid);
		
		gdb.addGrid(g2);		
		outGrid = gdb.getPrevious();
		assertEquals(3 * gdb.getStepWidth(), gdb.getPosition());
		assertEquals(g1, outGrid);
		
		gdb.addGrid(g3);		
		outGrid = gdb.getPrevious();
		assertEquals(4 * gdb.getStepWidth(), gdb.getPosition());
		assertEquals(g2, outGrid);
	}
	
	public void testGraphicBufferFromBytes()
	{
		GraphicDisplayBuffer gdb = new GraphicDisplayBuffer();
		byte[] aniBytes = new byte[200];
		aniBytes[0] = (byte) 0xFF;
		gdb.setDataFromBytes(aniBytes);
		Grid grid = gdb.getCurrent();
		boolean[][] boolArray = grid.getData();
		assertTrue(boolArray[0][0]);
	}
	

}
