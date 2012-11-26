package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;
import org.hacklace.animator.displaybuffer.Grid;

public class GraphicsDisplayBufferTest extends TestCase {

	public void testGrid() {
		GraphicDisplayBuffer gdb = new GraphicDisplayBuffer();
		Grid inGrid = new Grid(); 
		boolean[][] data = inGrid.getData();
		data[4][2] = true;
		data[1][3] = true;
		data[3][6] = true;

		gdb.addGrid(inGrid);
		
		Grid outGrid = gdb.getCurrent();
		
		assertEquals(inGrid, outGrid);
		
		System.out.println(outGrid.toString());
		
	}
	

}
