package org.hacklace.animator.displaybuffer;

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

	public boolean isValid() {
		return FontUtil.isValidSpecialChar(c);
	}

	@Override
	public int getNumColumns() {
		if (aniBytes == null)
			getAnimationBytes();
		return aniBytes.length;
	}

}
