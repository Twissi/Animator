package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.IllegalHacklaceConfigFileException;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.ModusByte;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.enums.StepWidth;

public abstract class DisplayBuffer implements Cloneable {

	protected boolean[][] data;

	public static final int MAX_COLUMNS = 200;

	protected ModusByte modusByte = new ModusByte();

	protected static int gridRows = IniConf.getInstance().rows();
	protected static int gridCols = IniConf.getInstance().columns();

	protected DisplayBuffer() {
		data = new boolean[MAX_COLUMNS][gridRows];
		modusByte = new ModusByte();
	}

	protected DisplayBuffer(ModusByte modusByte) {
		this.modusByte = modusByte;
	}

	protected void clearData() {
		for (int x = 0; x < MAX_COLUMNS; x++) {
			for (int y = 0; y < gridRows; y++) {
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
		if (x >= MAX_COLUMNS || y >= gridRows)
			return false;
		return data[x][y];
	}

	public void setValueAt(int x, int y, boolean value) {
		data[x][y] = value;
	}

	public int getStepWidth() {
		return modusByte.getStepWidth().getValue();
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

	public DisplayBuffer clone() {
		try {
			DisplayBuffer copy = (DisplayBuffer) super.clone();
			copy.modusByte = this.modusByte.clone();
			copy.data = new boolean[MAX_COLUMNS][gridRows];
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
	 * returns the total number of grids
	 * 
	 * @return
	 */
	public static int getNumGrids() {
		return MAX_COLUMNS / gridCols;
	}

	public int getRows() {
		return gridRows;
	}

	public int getCols() {
		return gridCols;
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
			int lineNumber) throws IllegalHacklaceConfigFileException {
		String modusByteString = cfgLine.substring(0, 3);
		ModusByte modusByte = new ModusByte(modusByteString, lineNumber);
		if (modusByte.isEOF()) {
			return null;
		}

		String restOfLine = getRestOfLine(cfgLine);
		AnimationType animationType = restOfLineAnalyzeType(restOfLine);
		DisplayBuffer displayBuffer = createDisplayBuffer(modusByte,
				animationType, restOfLine, lineNumber);
		return displayBuffer;

	}

	private static DisplayBuffer createDisplayBuffer(ModusByte modusByte,
			AnimationType animationType, String restOfLine, int lineNumber)
			throws IllegalHacklaceConfigFileException {
		switch (animationType) {
			case TEXT :
				return new TextDisplayBuffer(modusByte, restOfLine);
			case GRAPHIC :
				return new GraphicDisplayBuffer(modusByte, restOfLine,
						lineNumber);
			case REFERENCE :
				return new ReferenceDisplayBuffer(modusByte,
						restOfLine.charAt(1));
			case MIXED :
				return new MixedDisplayBuffer(modusByte, restOfLine);

		}
		return null;
	}

	/**
	 * 
	 * @param fullLine
	 * @return
	 */
	public static AnimationType fullLineAnalyzeType(String fullLine) {
		return restOfLineAnalyzeType(getRestOfLine(fullLine));
	}

	/**
	 * 
	 * @param restOfLine
	 *            configuration line without the first four chars for the modus
	 *            byte
	 * @return
	 */
	public static AnimationType restOfLineAnalyzeType(String restOfLine) {

		// 0 or 1 chars cannot be reference/graphic/mixed
		if (restOfLine.trim().length() < 2)
			return AnimationType.TEXT;

		if (restOfLineIsReference(restOfLine))
			return AnimationType.REFERENCE;

		// contains, but is not reference
		if (lineContainsReference(restOfLine))
			return AnimationType.MIXED;

		// contains no direct modes and no reference => is text
		if (!restOfLineContainsDirectMode(restOfLine))
			return AnimationType.TEXT;

		if (restOfLineIsGraphicOnly(restOfLine))
			return AnimationType.GRAPHIC;

		return AnimationType.MIXED;
	}

	public static String getRestOfLine(String fullLine) {
		if (fullLine.length() < 4)
			return "";
		return fullLine.substring(4);
	}

	public static boolean restOfLineContainsDirectMode(String restOfLine) {
		return restOfLineNumberOfDirectModes(restOfLine) > 0;
	}

	public static boolean fullLineContainsDirectMode(String fullLine) {
		return restOfLineContainsDirectMode(getRestOfLine(fullLine));
	}

	/**
	 * 
	 * @param restOfLine
	 * @return true for ~A etc, false for ~~, false for others, slightly
	 *         illegitimately true for "~A " (trailing spaces)
	 */
	public static boolean restOfLineIsReference(String restOfLine) {
		return restOfLine.startsWith("~") && restOfLine.trim().length() == 2
				&& restOfLine.charAt(1) != '~';
		// Actually "~A " would have to be mixed, but the user
		// may have entered the trailing space accidentally.
		// It's not really legitimate to cut it off, but
		// mixed buffers cannot be edited, so we do it anyway.
		// Instead of the trailing space the user should use delay.
	}

	public static boolean fullLineIsReference(String fullLine) {
		return restOfLineIsReference(getRestOfLine(fullLine));
	}

	/**
	 * 
	 * @param line
	 *            can be rest of line (without modus byte) or full line (with
	 *            modus byte)
	 * @return
	 */
	public static boolean lineContainsReference(String line) {
		if (line == null)
			return false;
		if (line.length() < 2)
			return false;
		if (!line.contains("~"))
			return false;
		// line now certainly contains ~
		char last = ' ';
		for (char c : line.toCharArray()) {
			if (last == '~' && c != '~') {
				// ~~ is not a reference, everything else is
				return true;
			}
			if (last == '~' && c == '~') {
				last = ' ';
				// prevent e.g. ~~A to be read as reference instead of as text ~
				// and A
			} else {
				last = c;
			}
		}
		return false;
	}

	public final static String DIRECT = "$FF";

	/**
	 * rest of line starts with $FF and ends with $FF, (or other char instead of
	 * ,)
	 * 
	 * @param fullLine
	 * @return
	 */
	public static boolean restOfLineIsGraphicOnly(String restOfLine) {
		if (!restOfLine.startsWith(DIRECT))
			return false;
		int length = restOfLine.length();
		if (length < 4)
			return false;
		String end = restOfLine.substring(length - 4, length - 1);
		// last char is , or space, leave out
		if (!end.equals(DIRECT))
			return false;
		if (restOfLineNumberOfDirectModes(restOfLine) == 2)
			return true;
		return false;
	}

	/**
	 * rest of line starts with $FF and ends with $FF, (or other char instead of
	 * ,)
	 * 
	 * @param fullLine
	 * @return
	 */
	public static boolean fullLineIsGraphicOnly(String fullLine) {
		return restOfLineIsGraphicOnly(getRestOfLine(fullLine));
	}

	/**
	 * How often does $FF occur? (Once for each start and end of direct mode)
	 * 
	 * @param restOfLine
	 * @return
	 */
	public static int restOfLineNumberOfDirectModes(String restOfLine) {
		int numberOfDirectModes = 0;
		int index = 0;
		while ((index = restOfLine.indexOf(DIRECT, index)) != -1) {
			numberOfDirectModes++;
			index += 3;
		}
		return numberOfDirectModes;
	}

	/**
	 * How often does $FF occur? (Once for each start and end of direct mode)
	 * 
	 * @param fullLine
	 * @return
	 */
	public static int fullLineNumberOfDirectModes(String fullLine) {
		return restOfLineNumberOfDirectModes(getRestOfLine(fullLine));
	}

	/**
	 * Generates the raw string used in config files
	 * 
	 * @return the raw string used in config files
	 */
	public abstract String getRawString();

	public int getMaxColumns() {
		return MAX_COLUMNS;
	}

}
