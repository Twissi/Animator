package org.hacklace.animator.configuration;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.displaybuffer.FontUtil;
import org.hacklace.animator.displaybuffer.Size;

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
		return FontUtil.isValidSpecialChar(c, errorContainer);
	}

	@Override
	public int getNumColumns() {
		if (aniBytes == null)
			getAnimationBytes();
		return aniBytes.length;
	}

}
