package org.hacklace.animator.configuration;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.displaybuffer.FontUtil;
import org.hacklace.animator.displaybuffer.Size;

public class EscapeChar extends TextElement implements Size {

	private char c;
	private int[] aniBytes;

	public EscapeChar(char c) {
		assert c == '^' || c == '$' || c == '~';
		this.c = c;

	}

	public int[] getAnimationBytes() {
		if (aniBytes == null)
			aniBytes = FontUtil.getMinimumBytesForChar(c);
		return aniBytes;
	}

	public String getRawString() {
		return "" + c + c;
	}

	public boolean isValid(ErrorContainer errorContainer) {
		return true;
	}

	@Override
	public int getNumColumns() {
		if (aniBytes == null)
			getAnimationBytes();
		return aniBytes.length;
	}

}
