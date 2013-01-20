/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.configuration;

import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.gui.AnimatorGui;

/**
 * 
 * @author monika
 * 
 *         immutable
 */
public abstract class AnimationPart {

	protected boolean[][] data;

	protected final static int GRID_ROWS = AnimatorGui.getIniConf().rows();

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

	public int getNumColumns() {
		return data.length;
	}

	public abstract int getNumBytes();

	public boolean[][] getData() {
		return data;
	}

}
