package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.ConversionUtil.convertAnimationByteTo7Booleans;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.enums.AnimationType;

public class TextDisplayBuffer extends DisplayBuffer implements Size {

	private String text;

	public TextDisplayBuffer() {
		super();
		setText("", new ErrorContainer()); // initializes data
	}

	public TextDisplayBuffer(FullConfigLine fullLine, ErrorContainer errorContainer) {
		super(fullLine.getModusByte(errorContainer));
		setText(fullLine.getRestOfLine(errorContainer).getModifiedRawString(), errorContainer);
	}

	public String getText() {
		return text;
	}

	/**
	 * side effect: update data (bits/bytes)
	 * 
	 * @param text
	 */
	public void setText(String text, ErrorContainer errorContainer) {

		this.text = text;

		byte[] animationBytes = FontUtil.getBytesForRawString(text, errorContainer);

		data = new boolean[animationBytes.length][];
		
		for (int i= 0; i<animationBytes.length; i++) {
			data[i] = convertAnimationByteTo7Booleans(animationBytes[i]);
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

	@Override
	public int getNumBytes() {
		return 1 // modus byte
		+ getNumColumns() //
		+ 1; // line end
	}

}
