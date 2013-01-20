/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.configuration;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.FontUtil;

public class RoofChar extends TextElement {

	private char c;
	private int[] aniBytes;

	public RoofChar(char c) {
		this.c = c;
	}

	@Override
	public int[] getAnimationBytes() {
		if (aniBytes == null)
			aniBytes = FontUtil.getMinimumBytesForSpecial(c);
		return aniBytes;
	}

	@Override
	public String getRawString() {
		return "^" + c;
	}

	@Override
	public boolean isValid(ErrorContainer errorContainer) {
		return FontUtil.isValidSpecialChar(c, errorContainer);
	}

	@Override
	public int getNumColumns() {
		if (aniBytes == null)
			getAnimationBytes();
		return aniBytes.length;
	}

}
