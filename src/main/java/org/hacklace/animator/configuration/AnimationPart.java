package org.hacklace.animator.configuration;

import org.hacklace.animator.IniConf;
import org.hacklace.animator.displaybuffer.Size;
import org.hacklace.animator.enums.AnimationType;

/**
 * 
 * @author monika
 *
 * immutable
 */
public abstract class AnimationPart implements Size {

	protected boolean[][] data;

	protected final static int GRID_ROWS = IniConf.getInstance().rows();

	public abstract AnimationType getAnimationType();

	@Override
	public String toString() {
		return "AnimationElement";
	}

	// Top left corner is (0,0)
	public boolean getColumnRow(int column, int row) {
		return data[column][row];
	}

	/**
	 * editable by clicking on the LEDs? True for graphic, false for text and
	 * reference
	 * 
	 * @return
	 */
	public abstract boolean isClickEditable();

	/**
	 * Generates the raw string used in config files
	 * 
	 * @return the raw string used in config files
	 */
	public abstract String getRawString();

	@Override
	public int getNumColumns() {
		return data.length;
	}

	@Override
	public abstract int getNumBytes();
	
	public boolean[][] getData() {
		return data;
	}

}
