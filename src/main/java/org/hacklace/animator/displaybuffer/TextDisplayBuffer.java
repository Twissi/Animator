package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.ConversionUtil.convertAnimationByteTo7Booleans;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.enums.AnimationType;

public class TextDisplayBuffer extends DisplayBuffer implements Size {

	private String text;

	public TextDisplayBuffer() {
		super();
		this.text = "";
	}

	public TextDisplayBuffer(FullConfigLine fullLine, ErrorContainer errorContainer) {
		super(fullLine.getModusByte(errorContainer));
		setText(fullLine.getRestOfLine().getValue(), errorContainer);
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
		clearData();
		this.text = text;

		byte[] animationBytes = FontUtil.getBytesForRawString(text, errorContainer);

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

	@Override
	public int getNumBytes() {
		return 1 // modus byte
		+ countUsedColumns() //
		+ 1; // line end
	}

	@Override
	public int countUsedColumns() {
		ErrorContainer errorContainer = new ErrorContainer();
		int usedCols = FontUtil.getIntsForRawString(getText(), errorContainer).length;
		return usedCols;
	}
}
