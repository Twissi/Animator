/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.configuration;

public class DirectMode {
	private String directModeString;

	/**
	 * cuts off the starting and ending $FF (and separation char)
	 * 
	 * @param restOfConfigurationLine
	 */
	public DirectMode(RestOfConfigLine restOfLine) {
		String line = restOfLine.getOriginalRawString();
		this.directModeString = line.substring(4, line.length() - 4);
	}

	/**
	 * 
	 * @param directModeString
	 *            without starting and ending $FF
	 */
	public DirectMode(String directModeString) {
		this.directModeString = directModeString;
	}

	public boolean isValid() {
		if (directModeString == null)
			return false;
		if ("".equals(directModeString))
			return true;
		// TODO
		return true;
	}

	public String getValue() {
		return directModeString;
	}
}
