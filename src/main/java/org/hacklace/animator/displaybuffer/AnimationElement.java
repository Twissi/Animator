package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.IniConf;
import org.hacklace.animator.enums.AnimationType;

public abstract class AnimationElement {
	protected boolean[][] data;

	protected final static int GRID_ROWS = IniConf.getInstance().rows();

	protected AnimationElement() {
	}

	/**
	 * Top left corner is (0,0) Note: For convenience this returns boolean false
	 * for non-existent coordinates.
	 * 
	 * @param x
	 *            right (column)
	 * @param y
	 *            down (row)
	 * @return
	 */
	public boolean getValueAt(int x, int y) {
		if (x >= data.length || y >= GRID_ROWS)
			return false;
		return data[x][y];
	}

	public void setValueAt(int x, int y, boolean value) {
		data[x][y] = value;
	}

	public abstract AnimationType getAnimationType();

	@Override
	public String toString() {
		return "AnimationElement";
	}

	public boolean getColumnRow(int column, int row) {
		return data[column][row];
	}
	
	/**
	 * true for Graphic, false for text and reference
	 * @return
	 */
	public abstract boolean isBitEditable();

	/**
	 * Generates the raw string used in config files
	 * 
	 * @return the raw string used in config files
	 */
	public abstract String getRawString();

}
