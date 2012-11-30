package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.displaybuffer.Grid;
import org.hacklace.animator.gui.FontUtil;

public class GridTest extends TestCase {
	
	public void testGrid() {
		Grid grid = new Grid(7, 5);
		
		grid.setColumnRow(4, 2, true);
		grid.setColumnRow(1,3,true);
		grid.setColumnRow(3,6,true);	
		
		assertTrue(grid.getColumnRow(4,2));
		assertTrue(grid.getColumnRow(1,3));
		assertTrue(grid.getColumnRow(3,6));	
	}

	public void testFontUtilIndex() {
		Grid grid = new Grid(7,5);
		grid.setDataFromBytes( FontUtil.getFiveBytesForIndex((int) 'W'));
		System.out.println(grid);
		assertFalse(grid.getColumnRow(0,6));
		assertTrue(grid.getColumnRow(0,5));
	}

	public void testFontUtilChar() {
		Grid grid = new Grid(7,5);
		grid.setDataFromBytes( FontUtil.getFiveBytesForChar('W'));
		System.out.println(grid);
		assertFalse(grid.getColumnRow(0,6));
		assertTrue(grid.getColumnRow(0,5));
	}
	
	public void testFontUtilSpecial() {
		Grid grid = new Grid(7,5);
		grid.setDataFromBytes( FontUtil.getFiveBytesForSpecial("^A"));
		System.out.println(grid);
		assertFalse(grid.getColumnRow(0,6));
		assertTrue(grid.getColumnRow(0,4));
	}
	
}
