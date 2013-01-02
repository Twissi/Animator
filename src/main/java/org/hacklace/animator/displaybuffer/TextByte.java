package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.displaybuffer.FontUtil.HIGHEST_INDEX;
import static org.hacklace.animator.displaybuffer.FontUtil.LOWEST_INDEX;

import java.util.List;

public class TextByte extends TextElement implements Size {

	private int[] aniBytes;
	
	// delegate
	private ByteElement byteElement;

	public TextByte(String fourChars) {
		byteElement = new ByteElement(fourChars);
	}

	@Override
	public boolean isValid() {
		return byteElement.isValid() && byteElement.getByteAsInt() >= LOWEST_INDEX
				&& byteElement.getByteAsInt() <= HIGHEST_INDEX;
	}
	
	public List<String> analyzeWarnings() {
		return byteElement.analyzeWarnings();
	}

	public List<String> analyzeErrors() {
		List<String> list = byteElement.analyzeErrors();
		int index = byteElement.getByteAsInt();
		if (index < LOWEST_INDEX || index > HIGHEST_INDEX) {
			list.add("Text byte " + byteElement.fourChars + "(" + index
					+ ") not allowed, must be between " + LOWEST_INDEX
					+ " and " + HIGHEST_INDEX + ".");
		}
		return list;
	}

	public int[] getAnimationBytes() {
		if (aniBytes == null)
			aniBytes = FontUtil.getMinimumBytesForIndex(byteElement.getByteAsInt());
		return aniBytes;
	}

	@Override
	public int getNumColumns() {
		if (aniBytes == null)
			getAnimationBytes();
		return aniBytes.length;

	}

	@Override
	public String getRawString() {
		return byteElement.fourChars;
	}
	
	@Override
	public String toString() {
		return "TextByte "+getRawString();
	}

}
