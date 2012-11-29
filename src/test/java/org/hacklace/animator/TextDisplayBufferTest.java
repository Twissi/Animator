package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.displaybuffer.Grid;
import org.hacklace.animator.displaybuffer.TextDisplayBuffer;

public class TextDisplayBufferTest extends TestCase {
	
	@SuppressWarnings("unused")
	private Grid g0;
	private Grid g1;
	private Grid g2;
	private Grid g3;
	
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
		TextDisplayBuffer tdb = new TextDisplayBuffer();
		
	}
	
	public void testPrevious() {
		
	}
	
	public void testNext() {
		
	}
}
