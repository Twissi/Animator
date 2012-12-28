package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.ConversionUtil;
import org.hacklace.animator.IllegalHacklaceConfigFileException;
import org.hacklace.animator.ModusByte;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.enums.StepWidth;

public abstract class DisplayBuffer implements Cloneable {

	protected boolean[][] data;

	public static final int ROWS = 7;

	public static final int COLUMNS = 5;

	public static final int MAX_COLUMNS = 200;

	protected int position;

	protected ModusByte modusByte;

	protected DisplayBuffer() {
		data = new boolean[MAX_COLUMNS][ROWS];
		position = 0;
		modusByte = new ModusByte();
	}
	
	protected DisplayBuffer(ModusByte modusByte) {
		this.modusByte = modusByte;
	}

	protected void clearData() {
		for (int x = 0; x < MAX_COLUMNS; x++) {
			for (int y = 0; y < ROWS; y++) {
				data[x][y] = false;
			}
		}
	}
	
	/**
	 * Top left corner is (0,0)
	 * @param x right (column)
	 * @param y down (row)
	 * @return
	 */
	public boolean getValueAt(int x, int y) {
		return data[x][y];
	}
	
	public void setValueAt(int x, int y, boolean value) {
		data[x][y] = value;
	}

	public int getStepWidth() {
		return modusByte.getStepWidth().getValue();
	}

	/*
	 * Real code
	 */

	public void rewind() {
		position = 0;
	}

	public void moveLeft() {
		position = Math.max(0, position - getStepWidth());
	}

	public void moveRight() {
		position = Math.min(position + getStepWidth(), MAX_COLUMNS - 1);
	}

	/*
	 * Getter/Setter
	 */

	private Grid getGrid(int offset) {
		Grid newGrid = new Grid(ROWS, COLUMNS);

		for (int column = 0; column < COLUMNS; column++) {
			for (int row = 0; row < ROWS; row++) {
				newGrid.setColumnRow(column, row, data[position + offset + column][row]);
			}
		}

		return newGrid;
	}

	public Grid getPrevious() {
		return getGrid(-2 * getStepWidth());
	}

	public Grid getCurrent() {
		return getGrid(-getStepWidth());
	}

	public Grid getNext() {
		return getGrid(0);
	}

	public int getPosition() {
		return position;
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

	public abstract AnimationType getAnimationType();

	@Override
	public String toString() {
		return "DisplayBuffer";
	}

	public DisplayBuffer clone()  {
		try {
			DisplayBuffer copy = (DisplayBuffer) super.clone();
			copy.modusByte = this.modusByte.clone();
			copy.data = new boolean[MAX_COLUMNS][ROWS];
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
	 * @return reference to status byte
	 */
	public ModusByte getStatusByte() {
		return this.modusByte;
	}
	
	/**
	 * returns the total number of grids
	 * @return
	 */
	public static int getNumGrids() {
		return MAX_COLUMNS / COLUMNS;
	}
	
	/**
	 * 
	 * @param cfgLine
	 * @param lineNumber
	 * @return a DisplayBuffer for the input line, or null for $00, (the last
	 *         line)
	 * @throws IllegalHacklaceConfigFileException
	 */
	public static DisplayBuffer createBufferFromLine(String cfgLine,
			int lineNumber)
			throws IllegalHacklaceConfigFileException {
		String statusByteString = cfgLine.substring(0, 3);
		ModusByte modusByte = new ModusByte(statusByteString, lineNumber);
		if (modusByte.isEOF()) {
			return null;
		}
		
		// text or graphic animation?
		StepWidth stepWidth = modusByte.getStepWidth();
		DisplayBuffer buffer = null;
		String restOfLine = cfgLine.substring(4);
		if (restOfLine.startsWith("~")) {
			char letter = restOfLine.charAt(1);
			ReferenceDisplayBuffer referenceDisplayBuffer = new ReferenceDisplayBuffer(
					letter);
	
			buffer = referenceDisplayBuffer;
		} else if (stepWidth == StepWidth.ONE) {
			TextDisplayBuffer textDisplayBuffer = new TextDisplayBuffer(
					restOfLine);
			buffer = textDisplayBuffer;
		} else if (restOfLine.startsWith("$FF")) {
			GraphicDisplayBuffer graphicDisplayBuffer = new GraphicDisplayBuffer();
			byte[] aniBytes = ConversionUtil.createByteArrayFromString(
					restOfLine.substring(4, restOfLine.length() - 4),
					lineNumber); // cut off $FF in beginning and end
			graphicDisplayBuffer.setDataFromBytes(aniBytes);
			buffer = graphicDisplayBuffer;
		} else {
			MixedDisplayBuffer mixedDisplayBuffer = new MixedDisplayBuffer(
					restOfLine);
			buffer = mixedDisplayBuffer;
		}
		assert (buffer != null);
		buffer.setDirection(modusByte.getDirection());
		buffer.setSpeed(modusByte.getSpeed());
		buffer.setStepWidth(stepWidth);
		buffer.setDelay(modusByte.getDelay());
		return buffer;
	}
	
	/**
	 * Generates the raw string used in config files
	 * @return the raw string used in config files
	 */
	public String getRawString() {
		ModusByte modusByte = getStatusByte();
		StringBuilder stringBuilder = new StringBuilder();
		String statusByteString = ConversionUtil.convertByteToString(modusByte
				.getByte());
		stringBuilder.append(statusByteString).append(",");
		AnimationType animationType = getAnimationType();
		if (animationType == AnimationType.TEXT) {
			TextDisplayBuffer textDisplayBuffer = (TextDisplayBuffer) this;
			stringBuilder.append(textDisplayBuffer.getText());
		} else if (animationType == AnimationType.GRAPHIC) {
			GraphicDisplayBuffer graphicDisplayBuffer = (GraphicDisplayBuffer) this;
			byte[] columns = graphicDisplayBuffer.getColumnsAsBytes();
			stringBuilder.append("$FF ");
			for (byte column : columns) {
				stringBuilder.append(ConversionUtil.convertByteToString(column))
						.append(" ");
			}
			stringBuilder.append("$FF,");
		} else if (animationType == AnimationType.REFERENCE) {
			ReferenceDisplayBuffer referenceDisplayBuffer = (ReferenceDisplayBuffer) this;
			stringBuilder.append("~").append(
					referenceDisplayBuffer.getLetter());
		} else {
			MixedDisplayBuffer mixedDisplayBuffer = (MixedDisplayBuffer) this;
			stringBuilder.append(mixedDisplayBuffer.getStringValue());
		}
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}

}
