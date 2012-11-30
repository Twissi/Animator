package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;
import org.hacklace.animator.displaybuffer.Grid;

public class GraphicDisplayBufferTest extends TestCase {

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

		/*
		 * First grid
		 */

		g1.setColumnRow(4, 2, true);
		g1.setColumnRow(1, 3, true);
		g1.setColumnRow(3, 6, true);
		g1.setColumnRow(3, 1, true);

		/*
		 * Second grid
		 */

		g2.setColumnRow(4, 2, true);
		g2.setColumnRow(1, 3, true);
		g2.setColumnRow(3, 6, true);
		g2.setColumnRow(3, 1, true);

		/*
		 * Third grid
		 */

		g3.setColumnRow(0, 0, true);
		g3.setColumnRow(0, 1, true);
		g3.setColumnRow(0, 2, true);
		g3.setColumnRow(0, 3, true);

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

	public void testGraphicBufferFromBytes() {
		GraphicDisplayBuffer gdb = new GraphicDisplayBuffer();
		byte[] aniBytes = new byte[200];
		aniBytes[0] = (byte) 0xFF;
		gdb.setDataFromBytes(aniBytes);
		Grid grid = gdb.getCurrent();
		assertTrue(grid.getColumnRow(0, 0));
		assertTrue(grid.getColumnRow(0, 1));
		assertTrue(grid.getColumnRow(0, 2));
		assertTrue(grid.getColumnRow(0, 3));
		assertTrue(grid.getColumnRow(0, 4));
		assertTrue(grid.getColumnRow(0, 5));
		assertTrue(grid.getColumnRow(0, 6));
		assertTrue(!grid.getColumnRow(1, 0));
		assertTrue(!grid.getColumnRow(1, 1));
		assertTrue(!grid.getColumnRow(1, 2));
		assertTrue(!grid.getColumnRow(1, 3));
		assertTrue(!grid.getColumnRow(1, 4));
		assertTrue(!grid.getColumnRow(1, 5));
		assertTrue(!grid.getColumnRow(1, 6));
	}
	public void testClone() {
		GraphicDisplayBuffer gdb1 = new GraphicDisplayBuffer();
		Grid grid1 = new Grid();
		grid1.setColumnRow(0, 0, true);
		gdb1.addGrid(grid1);
		GraphicDisplayBuffer gdb2 = (GraphicDisplayBuffer) gdb1.clone();
		assertNotNull(gdb2);
		assertEquals(gdb1.getDirection(), gdb2.getDirection());
		assertEquals(gdb1.getSpeed(), gdb2.getSpeed());
		assertEquals(gdb1.getDelay(), gdb2.getDelay());
		assertEquals(gdb1.getCurrent(), gdb2.getCurrent());
		Grid grid2 = new Grid();
		grid2.setColumnRow(0, 1, true);
		gdb1.addGrid(grid2);
		Grid grid3 = new Grid();
		grid3.setColumnRow(1, 0, true);
		gdb2.addGrid(grid3);
		assert (!gdb1.getCurrent().equals(gdb2.getCurrent()));

		assertTrue(!gdb1.getColumnRow(2, 3));
		gdb1.setColumnRow(2, 3, true);
		assertTrue(gdb1.getColumnRow(2, 3));
		assertTrue(!gdb2.getColumnRow(2, 3));
	}

}
