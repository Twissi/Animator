package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.displaybuffer.FontUtil.HIGHEST_INDEX;
import static org.hacklace.animator.displaybuffer.FontUtil.LOWEST_INDEX;

import java.util.List;

import org.hacklace.animator.ErrorContainer;

public class TextByte extends TextElement implements Size {

	private int[] aniBytes;
	
	// delegate
	private ByteElement byteElement;

	public TextByte(String fourChars, ErrorContainer errorContainer) {
		byteElement = new ByteElement(fourChars, errorContainer);
	}

	@Override
	public boolean isValid(ErrorContainer errorContainer) {
		return byteElement.isValid(errorContainer) && byteElement.getByteAsInt(errorContainer) >= LOWEST_INDEX
				&& byteElement.getByteAsInt(errorContainer) <= HIGHEST_INDEX;
	}
	
	public List<String> analyzeWarnings() {
		return byteElement.analyzeWarnings();
	}

	public List<String> analyzeErrors() {
		List<String> list = byteElement.analyzeErrors();
		int index = byteElement.getByteAsInt(new ErrorContainer()); // TODO make error handling more consistent
		if (index < LOWEST_INDEX || index > HIGHEST_INDEX) {
			list.add("Text byte " + byteElement.fourChars + "(" + index
					+ ") not allowed, must be between " + LOWEST_INDEX
					+ " and " + HIGHEST_INDEX + ".");
		}
		return list;
	}

	public int[] getAnimationBytes() {
		if (aniBytes == null)
			aniBytes = FontUtil.getMinimumBytesForIndex(byteElement.getByteAsInt(new ErrorContainer())); // TODO maybe find different solution
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
