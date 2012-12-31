package org.hacklace.animator.configuration;

import org.hacklace.animator.ModusByte;
import org.hacklace.animator.enums.AnimationType;

/**
 * Hacklace configuration line with modus byte
 * 
 * @author monika
 * 
 */
public class FullConfigLine {

	private String fullLineString;

	public FullConfigLine(String fullLineString) {
		this.fullLineString = fullLineString;
	}

	public FullConfigLine(ModusByte modusByte, DirectMode directMode) {
		this.fullLineString = modusByte.getRawString() + "$FF "
				+ directMode.getValue() + "$FF,";
	}

	public FullConfigLine(ModusByte modusByte, RestOfConfigLine restOfLine) {
		this.fullLineString = modusByte.getRawString() + restOfLine.getValue();
	}

	public boolean isValid() {
		if (fullLineString == null)
			return false;
		// TODO
		return true;
	}

	boolean containsDirectMode() {
		return getRestOfLine().containsDirectMode();
	}

	boolean isReference() {
		return getRestOfLine().isReference();
	}

	/**
	 * rest of line starts with $FF and ends with $FF, (or other char instead of
	 * ,)
	 * 
	 * @param fullLine
	 * @return
	 */
	boolean isGraphicOnly() {
		return getRestOfLine().isGraphicOnly();
	}

	/**
	 * How often does $FF occur? (Once for each start and end of direct mode)
	 * 
	 * @param fullLine
	 * @return
	 */
	int numberOfDirectModes() {
		return getRestOfLine().numberOfDirectModes();
	}

	/**
	 * 
	 * @param fullLine
	 * @return
	 */
	public AnimationType analyzeType() {
		return getRestOfLine().analyzeType();
	}

	boolean containsReference() {
		return getRestOfLine().containsReference();
	}

	public ModusByte getModusByte() {
		String modusByteString = fullLineString.substring(0, 3); // first 4
																	// chars
		ModusByte modusByte = new ModusByte(modusByteString);
		return modusByte;
	}

	public RestOfConfigLine getRestOfLine() {
		if (fullLineString.length() <= 4)
			return new RestOfConfigLine("");
		return new RestOfConfigLine(fullLineString.substring(4));
	}

	public String getValue() {
		return fullLineString;
	}

	@Override
	public String toString() {
		return fullLineString;
	}

	/**
	 * for reference animations - consumer needs to assure that this is a valid
	 * reference animation
	 * 
	 * @return
	 */
	public char getSixthChar() {
		assert (fullLineString.length() == 6);
		return fullLineString.charAt(5);
	}

}
