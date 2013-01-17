package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.ModusByte;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.configuration.RestOfConfigLine;
import org.hacklace.animator.enums.AnimationType;

public class TextDisplayBuffer extends DisplayBuffer implements Size {

	protected RestOfConfigLine restOfLine; 

	public TextDisplayBuffer() {
		super();
		setText("", new ErrorContainer()); // initializes data
	}
	
	protected TextDisplayBuffer(ModusByte modusByte) {
		super(modusByte);
		setText("", new ErrorContainer()); // initializes data
	}

	public TextDisplayBuffer(FullConfigLine fullLine, ErrorContainer errorContainer) {
		super(fullLine.getModusByte(errorContainer));
		setRestOfConfigLine(fullLine.getRestOfLine(errorContainer));
	}

	public String getText() {
		return restOfLine.getModifiedRawString();
	}

	/**
	 * side effect: update data (bits/bytes)
	 * 
	 * @param text
	 */
	public void setText(String originalRawString, ErrorContainer errorContainer) {
        setRestOfConfigLine(new RestOfConfigLine(originalRawString, errorContainer));
	}
	
	private void setRestOfConfigLine(RestOfConfigLine restOfLine) {
		this.restOfLine = restOfLine;
		this.data = restOfLine.getLeds();
	}

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.TEXT;
	}

	@Override
	public String toString() {
		return getAnimationType().getDescription() + " " + restOfLine.getModifiedRawString();
	}

	@Override
	public String getRawStringForRestOfLine() {
		return restOfLine.getModifiedRawString();
	}

	@Override
	public int getNumBytes() {
		return 1 // modus byte
		+ restOfLine.getNumBytes()
		+ 1; // line end
	}

}
