package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.ConversionUtil.convertAnimationByteTo7Booleans;

import org.hacklace.animator.IllegalHacklaceConfigLineException;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.enums.AnimationType;

public class TextDisplayBuffer extends DisplayBuffer {

	private String text;

	public TextDisplayBuffer() {
		super();
		this.text = "";
	}

	public TextDisplayBuffer(FullConfigLine fullLine) throws IllegalHacklaceConfigLineException {
		super(fullLine.getModusByte());
		setText(fullLine.getRestOfLine().getValue());
	}

	public String getText() {
		return text;
	}

	/**
	 * side effect: update data (bits/bytes)
	 * 
	 * @param text
	 */
	public void setText(String text) {
		clearData();
		this.text = text;

		byte[] animationBytes = FontUtil.getBytesForRawString(text);

		clearData();
		
		int i = 0;
		for (byte aniByte : animationBytes) {
			boolean[] bits = convertAnimationByteTo7Booleans(aniByte);
			data[i++] = bits;
		}
	}

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.TEXT;
	}

	@Override
	public String toString() {
		return getAnimationType().getDescription() + " " + text;
	}

	@Override
	public String getRawStringForRestOfLine() {
		return getText();
	}

}
