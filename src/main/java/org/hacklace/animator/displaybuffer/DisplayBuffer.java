package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.IllegalHacklaceConfigLineException;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.ModusByte;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.enums.StepWidth;

public abstract class DisplayBuffer implements Cloneable {

	protected boolean[][] data = new boolean[MAX_COLUMNS][GRID_ROWS];

	public static final int MAX_COLUMNS = IniConf.getInstance().maxColumns();

	protected ModusByte modusByte = new ModusByte();

	protected final static int GRID_ROWS = IniConf.getInstance().rows();
	protected final static int GRID_COLS = IniConf.getInstance().columns();

	protected DisplayBuffer() {
		modusByte = new ModusByte();
	}

	protected DisplayBuffer(ModusByte modusByte) {
		this.modusByte = modusByte;
	}

	protected void clearData() {
		for (int x = 0; x < MAX_COLUMNS; x++) {
			for (int y = 0; y < GRID_ROWS; y++) {
				data[x][y] = false;
			}
		}
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
		if (x >= MAX_COLUMNS || y >= GRID_ROWS)
			return false;
		return data[x][y];
	}

	public void setValueAt(int x, int y, boolean value) {
		data[x][y] = value;
	}

	public StepWidth getStepWidth() {
		return modusByte.getStepWidth();
	}

	public Direction getDirection() {
		return this.modusByte.getDirection();
	}

	public Speed getSpeed() {
		return this.modusByte.getSpeed();
	}

	public Delay getDelay() {
		return this.modusByte.getDelay();
	}

	public void setDirection(Direction direction) {
		this.modusByte.setDirection(direction);
	}

	public void setDelay(Delay delay) {
		this.modusByte.setDelay(delay);
	}

	public void setStepWidth(StepWidth stepWidth) {
		this.modusByte.setStepWidth(stepWidth);
	}

	public void setSpeed(Speed speed) {
		this.modusByte.setSpeed(speed);
	}

	public ModusByte getModusByte() {
		return modusByte;
	}

	public abstract AnimationType getAnimationType();

	@Override
	public String toString() {
		return "DisplayBuffer";
	}

	public DisplayBuffer clone() {
		try {
			DisplayBuffer copy = (DisplayBuffer) super.clone();
			copy.modusByte = this.modusByte.clone();
			copy.data = new boolean[MAX_COLUMNS][GRID_ROWS];
			for (int colIndex = 0; colIndex < this.data.length; colIndex++) {
				boolean[] column = this.data[colIndex];
				copy.data[colIndex] = column.clone();
			}
			return copy;
		} catch (CloneNotSupportedException e) {
			return null;
		}

	}

	public boolean getColumnRow(int column, int row) {
		return data[column][row];
	}

	/**
	 * 
	 * @param cfgLine
	 * @return a DisplayBuffer for the input line, or null for $00, (the last
	 *         line)
	 * @throws IllegalHacklaceConfigLineException
	 */
	public static DisplayBuffer createBufferFromLine(FullConfigLine fullLine)
			throws IllegalHacklaceConfigLineException {
		ModusByte modusByte = fullLine.getModusByte();
		if (modusByte.isEOF()) {
			return null;
		}

		AnimationType animationType = fullLine.analyzeType();

		switch (animationType) {
		case TEXT:
			return new TextDisplayBuffer(fullLine);
		case GRAPHIC:
			return new GraphicDisplayBuffer(fullLine);
		case REFERENCE:
			return new ReferenceDisplayBuffer(fullLine);
		case MIXED:
			return new MixedDisplayBuffer(fullLine);
		}
		return null;
	}

	/**
	 * Generates the raw string used in config files for the whole line
	 * including modus byte
	 * 
	 * @return the raw string used in config files
	 */
	public final FullConfigLine getRawString() {
		return new FullConfigLine(modusByte.getRawString()
				+ getRawStringForRestOfLine());
	}

	/**
	 * without modus byte
	 * 
	 * @return
	 */
	public abstract String getRawStringForRestOfLine();

	/**
	 * under certain circumstances the buffer cannot be saved. modusByte 0 means
	 * "EOF" so this can't be used.
	 * 
	 * @return
	 */
	public boolean isSaveable() {
		return (modusByte.getByte() != 0);
	}

}
