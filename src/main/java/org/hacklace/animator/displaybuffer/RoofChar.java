package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.ErrorContainer;

public class RoofChar extends TextElement implements Size {

	private char c;
	private int[] aniBytes;

	public RoofChar(char c) {
		this.c = c;
	}

	public int[] getAnimationBytes() {
		if (aniBytes == null)
			aniBytes = FontUtil.getMinimumBytesForSpecial(c);
		return aniBytes;
	}

	public String getRawString() {
		return "^" + c;
	}

	public boolean isValid(ErrorContainer errorContainer) {
		if (!FontUtil.isValidSpecialChar(c)) {
			errorContainer
					.addError("^"
							+ c
							+ " is not valid. Must be between ^"
							+ (char) (FontUtil.LOWEST_SPECIAL_INDEX - FontUtil.SPECIAL_CHAR_OFFSET)
							+ " and "
							+ (char) (FontUtil.HIGHEST_INDEX - FontUtil.SPECIAL_CHAR_OFFSET)
							+ ".");
			return false;
		}
		return true;
	}

	@Override
	public int getNumColumns() {
		if (aniBytes == null)
			getAnimationBytes();
		return aniBytes.length;
	}

}
