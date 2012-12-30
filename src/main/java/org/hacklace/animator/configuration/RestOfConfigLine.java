package org.hacklace.animator.configuration;

import org.hacklace.animator.enums.AnimationType;

/**
 * Hacklace configuration line without modus byte
 * 
 * @author monika
 * 
 */

public class RestOfConfigLine {

	private String restOfLineString;
	public final static String DIRECT = "$FF";

	public RestOfConfigLine(String restOfLineString) {
		this.restOfLineString = restOfLineString;
	}

	public boolean isValid() {
		if (restOfLineString == null)
			return false;
		if ("".equals(restOfLineString))
			return true;
		// TODO
		return true;
	}

	/**
	 * How often does $FF occur? (Once for each start and end of direct mode)
	 * 
	 * @param restOfLine
	 * @return
	 */
	int numberOfDirectModes() {
		int numberOfDirectModes = 0;
		int index = 0;
		while ((index = restOfLineString.indexOf(DIRECT, index)) != -1) {
			numberOfDirectModes++;
			index += 3;
		}
		return numberOfDirectModes;
	}

	/**
	 * rest of line starts with $FF and ends with $FF, (or other char instead of
	 * ,)
	 * 
	 * @param fullLine
	 * @return
	 */
	boolean isGraphicOnly() {
		if (!restOfLineString.startsWith(DIRECT))
			return false;
		int length = restOfLineString.length();
		if (length < 4)
			return false;
		String end = restOfLineString.substring(length - 4, length - 1);
		// last char is , or space, leave out
		if (!end.equals(RestOfConfigLine.DIRECT))
			return false;
		if (numberOfDirectModes() == 2)
			return true;
		return false;
	}

	/**
	 * 
	 * @param restOfLine
	 *            configuration line without the first four chars for the modus
	 *            byte
	 * @return
	 */
	public AnimationType analyzeType() {

		// 0 or 1 chars (plus spaces) cannot be reference/graphic/mixed
		if (restOfLineString.trim().length() < 2)
			return AnimationType.TEXT;

		if (isReference())
			return AnimationType.REFERENCE;

		// contains, but is not reference
		if (containsReference())
			return AnimationType.MIXED;

		// contains no direct modes and no reference => is text
		if (!containsDirectMode())
			return AnimationType.TEXT;

		if (isGraphicOnly())
			return AnimationType.GRAPHIC;

		return AnimationType.MIXED;
	}

	boolean containsDirectMode() {
		return numberOfDirectModes() > 0;
	}

	/**
	 * 
	 * @param restOfLine
	 * @return true for ~A etc, false for ~~, false for others
	 */
	boolean isReference() {
		return restOfLineString.startsWith("~")
				&& restOfLineString.length() == 2
				&& restOfLineString.charAt(1) != '~';
	}

	/**
	 * 
	 * @param line
	 *            can be rest of line (without modus byte) or full line (with
	 *            modus byte)
	 * @return
	 */
	boolean containsReference() {
		if (restOfLineString == null)
			return false;
		if (restOfLineString.length() < 2)
			return false;
		if (!restOfLineString.contains("~"))
			return false;
		// line now certainly contains ~
		char last = ' ';
		for (char c : restOfLineString.toCharArray()) {
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

	public String getValue() {
		return restOfLineString;
	}

	@Override
	public String toString() {
		return restOfLineString;
	}

	/**
	 * the consumer has to ensure that this is only called on valid graphic-only
	 * configuration lines
	 * 
	 * @return essentially cuts off the first four and last four chars; "" for
	 *         restOfLines of length 8 and less
	 */

	public DirectMode getDirectMode() {
		return new DirectMode(this);
	}

}
