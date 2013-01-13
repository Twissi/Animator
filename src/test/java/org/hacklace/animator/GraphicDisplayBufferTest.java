package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;

public class GraphicDisplayBufferTest extends TestCase {

	protected void setUp() {

	}

	protected void tearDown() {

	}

	public void testGraphicBufferFromBytes() {
		//GraphicDisplayBuffer gdb = new GraphicDisplayBuffer();
		// byte[] aniBytes = new byte[200];
		// aniBytes[0] = (byte) 0xFF;
		// gdb.setDataFromBytes(aniBytes);
		// Grid grid = gdb.getCurrent();
		// assertTrue(grid.getColumnRow(0, 0));
		// assertTrue(grid.getColumnRow(0, 1));
		// assertTrue(grid.getColumnRow(0, 2));
		// assertTrue(grid.getColumnRow(0, 3));
		// assertTrue(grid.getColumnRow(0, 4));
		// assertTrue(grid.getColumnRow(0, 5));
		// assertTrue(grid.getColumnRow(0, 6));
		// assertTrue(!grid.getColumnRow(1, 0));
		// assertTrue(!grid.getColumnRow(1, 1));
		// assertTrue(!grid.getColumnRow(1, 2));
		// assertTrue(!grid.getColumnRow(1, 3));
		// assertTrue(!grid.getColumnRow(1, 4));
		// assertTrue(!grid.getColumnRow(1, 5));
		// assertTrue(!grid.getColumnRow(1, 6));
	}

	public void testClone() {
		// GraphicDisplayBuffer gdb1 = new GraphicDisplayBuffer();
		// Grid grid1 = new Grid();
		// grid1.setColumnRow(0, 0, true);
		// gdb1.addGrid(grid1);
		// GraphicDisplayBuffer gdb2 = (GraphicDisplayBuffer) gdb1.clone();
		// assertNotNull(gdb2);
		// assertEquals(gdb1.getDirection(), gdb2.getDirection());
		// assertEquals(gdb1.getSpeed(), gdb2.getSpeed());
		// assertEquals(gdb1.getDelay(), gdb2.getDelay());
		// // assertEquals(gdb1.getCurrent(), gdb2.getCurrent());
		// Grid grid2 = new Grid();
		// grid2.setColumnRow(0, 1, true);
		// gdb1.addGrid(grid2);
		// Grid grid3 = new Grid();
		// grid3.setColumnRow(1, 0, true);
		// gdb2.addGrid(grid3);
		// // assert (!gdb1.getCurrent().equals(gdb2.getCurrent()));
		//
		// assertTrue(!gdb1.getColumnRow(2, 3));
		// gdb1.setColumnRow(2, 3, true);
		// assertTrue(gdb1.getColumnRow(2, 3));
		// assertTrue(!gdb2.getColumnRow(2, 3));
	}

	public void testToRawString() {
		GraphicDisplayBuffer buf = new GraphicDisplayBuffer();
		buf.toggleColumnRow(0, 0);
		String rawStringWithoutModusByte = buf.getFullConfigLine().getOriginalString()
				.substring(4);
		assertEquals("$FF,$01,$00,$00,$00,$00,$FF,", rawStringWithoutModusByte);
	}

	public void testFromRawString() {
		String rawString = "$04,$FF $01 $FF,";
		FullConfigLine fullLine = new FullConfigLine(rawString);
		ErrorContainer errorContainer = new ErrorContainer();
		DisplayBuffer buf = DisplayBuffer.createBufferFromLine(fullLine, errorContainer);
		boolean topLeftLed = buf.getValueAtColumnRow(0, 0);
		boolean bottomLeftLed = buf.getValueAtColumnRow(0, 6);
		assertTrue(topLeftLed);
		assertFalse(bottomLeftLed);
	}

}
