package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.ErrorContainer;

public class SimpleChar extends TextElement implements Size {

	private char c;
	private int[] aniBytes;

	public SimpleChar(char c) {
		this.c = c;
	}

	public int[] getAnimationBytes() {
		if (aniBytes == null)
			aniBytes = FontUtil.getMinimumBytesForChar(c);
		return aniBytes;
	}

	public String getRawString() {
		return "" + c;
	}

	public boolean isValid(ErrorContainer errorContainer) {
		return FontUtil.isValidHacklaceChar(c);
	}

	@Override
	public int getNumColumns() {
		if (aniBytes == null)
			getAnimationBytes();
		return aniBytes.length;
	}

}