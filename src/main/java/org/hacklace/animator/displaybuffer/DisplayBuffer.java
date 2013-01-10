package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.ModusByte;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.enums.StepWidth;

public abstract class DisplayBuffer implements Cloneable, Size {

	protected boolean[][] data;

	protected ModusByte modusByte = new ModusByte();

	protected final static int GRID_ROWS = IniConf.getInstance().rows();

	protected DisplayBuffer() {
		modusByte = new ModusByte();
	}

	protected DisplayBuffer(ModusByte modusByte) {
		this.modusByte = modusByte;
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
			copy.data = new boolean[this.data.length][GRID_ROWS];
			for (int colIndex = 0; colIndex < this.data.length; colIndex++) {
				boolean[] column = this.data[colIndex];
				copy.data[colIndex] = column.clone();
			}
			return copy;
		} catch (CloneNotSupportedException e) {
			return null;
		}

	}

	public boolean getValueAtColumnRow(int column, int row) {
		if (column > data.length-1) {
			// TODO find out when this happens and prevent it
			return false;
		}
		return data[column][row];
	}

	/**
	 * 
	 * @param cfgLine
	 * @return a DisplayBuffer for the input line, or null for $00, (the last
	 *         line)
	 */
	public static DisplayBuffer createBufferFromLine(FullConfigLine fullLine, ErrorContainer errorContainer) {
		ModusByte modusByte = fullLine.getModusByte(errorContainer);
		if (modusByte.isEOF()) {
			return null;
		}

		AnimationType animationType = fullLine.getRestOfLine(errorContainer).analyzeType();

		switch (animationType) {
		case TEXT:
			return new TextDisplayBuffer(fullLine, errorContainer);
		case GRAPHIC:
			return new GraphicDisplayBuffer(fullLine, errorContainer);
		case REFERENCE:
			return new ReferenceDisplayBuffer(fullLine, errorContainer);
		case MIXED:
			return new MixedDisplayBuffer(fullLine, errorContainer);
		}
		return null;
	}

	/**
	 * Generates the raw string used in config files for the whole line
	 * including modus byte
	 * 
	 * @return the raw string used in config files
	 */
	public final FullConfigLine getFullConfigLine() {
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
		ErrorContainer errorContainer = new ErrorContainer();
		getFullConfigLine().getRestOfLine(errorContainer);
		return (modusByte.getByte() != 0 && errorContainer.isErrorFree());
	}

	/**
	 * for the UI, especially for mixed buffers
	 * 
	 * counts the number of columns used for the animation, includes empty columns.
	 */
	@Override
	public int getNumColumns() {
		return data.length;
	}

	/**
	 * reference animations only need 2 bytes, not number of columns; also: add 1 byte for modus byte and 1 byte for 0 delimiter
	 */
	@Override
	public abstract int getNumBytes();
	
}
