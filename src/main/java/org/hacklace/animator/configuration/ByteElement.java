/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.configuration;

import java.util.LinkedList;
import java.util.List;

import org.hacklace.animator.ConversionUtil;
import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.IniConf;

/**
 * Something represented as $nn, in the config files. Can end up as one byte in
 * the Hacklace EEPROM in case of graphic or as 1-6 bytes in case of a
 * character.
 * 
 * @author monika
 * 
 */

public class ByteElement {

	public static boolean isDirectModeSwitchByte(String fourChars) {
		return fourChars.startsWith("$FF");
	}

	protected String fourChars; // all 4 chars "$nn," (any fourth char)
	protected String threeChars; // first 3 chars $nn
	protected String twoChars; // second and third char, nn (should be hex)

	public ByteElement(String fourChars, ErrorContainer errorContainer) {
		assert (fourChars != null);
		assert (fourChars.length() == 4);
		this.fourChars = fourChars;
		this.threeChars = fourChars.substring(0, 3);
		this.twoChars = fourChars.substring(1, 3);
		isValid(errorContainer);
	}
	
	public boolean isValid(ErrorContainer errorContainer) {
		// technically any char is allowed as fourth char / separator
		return ConversionUtil.isHexSequence(threeChars, errorContainer);
	}

	public List<String> analyzeErrors() {
		List<String> errorList = new LinkedList<String>();
		if (fourChars.charAt(0) != '$')
			errorList.add("Hex string must start with $. (" + fourChars + ")");
		if (!twoChars.matches("^[0-9A-F]{2}"))
			errorList.add("Both hex digits must be 0-9 or A-F. (" + fourChars
					+ ")");
		return errorList;
	}

	public List<String> analyzeWarnings() {
		List<String> warningList = new LinkedList<String>();
		final String separators = IniConf.separators;
		if (separators.indexOf(fourChars.charAt(3)) == -1)
			warningList.add("The fourth char should be " + separators + " ("
					+ fourChars + ")");
		return warningList;
	}

	/**
	 * 
	 * @return -128 to 127
	 */
	public byte getByte(ErrorContainer errorContainer) {
		return ConversionUtil.convertStringToByte(threeChars, errorContainer);
	}

	/**
	 * 
	 * @return 0 to 255
	 */
	public int getByteAsInt(ErrorContainer errorContainer) {
		return ConversionUtil.convertStringToByte(threeChars, errorContainer);
	}

}
