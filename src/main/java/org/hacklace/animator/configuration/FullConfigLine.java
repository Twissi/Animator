/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.configuration;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.ModusByte;

/**
 * Hacklace configuration line with modus byte
 * 
 * @author monika
 * 
 */
public class FullConfigLine {

	private String originalFullLineString;

	public FullConfigLine(String fullLineString) {
		assert fullLineString != null;
		this.originalFullLineString = fullLineString;
	}

	public FullConfigLine(ModusByte modusByte, DirectMode directMode) {
		this.originalFullLineString = modusByte.getRawString() + "$FF "
				+ directMode.getValue() + "$FF,";
	}

	public FullConfigLine(ModusByte modusByte, RestOfConfigLine restOfLine) {
		this.originalFullLineString = modusByte.getRawString() + restOfLine.getOriginalRawString();
	}

	public ModusByte getModusByte(ErrorContainer errorContainer) {
		if (originalFullLineString.length() < 4) {
			errorContainer
					.addError(originalFullLineString
							+ "needs to have at least length 4 for the modus byte ($nn,).");
			return new ModusByte();
		}
		String modusByteString = originalFullLineString.substring(0, 3); // first 4
																	// chars
		return new ModusByte(modusByteString, errorContainer);
	}

	public RestOfConfigLine getRestOfLine(ErrorContainer errorContainer) {
		if (originalFullLineString.length() <= 4)
			return new RestOfConfigLine("", errorContainer);
		return new RestOfConfigLine(originalFullLineString.substring(4), errorContainer);
	}

	public String getOriginalString() {
		return originalFullLineString;
	}

	@Override
	public String toString() {
		return originalFullLineString;
	}

}
