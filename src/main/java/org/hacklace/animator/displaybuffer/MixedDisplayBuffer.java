package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.configuration.RestOfConfigLine;
import org.hacklace.animator.enums.AnimationType;

public class MixedDisplayBuffer extends TextDisplayBuffer {

	//private RestOfConfigLine restOfLine;
	private boolean[] clickEditable;

	public MixedDisplayBuffer(FullConfigLine fullLine,
			ErrorContainer errorContainer) {
		super(fullLine.getModusByte(errorContainer));
		setRestOfLine(fullLine.getRestOfLine(errorContainer));
	}

	public MixedDisplayBuffer() {
		super();
		setRestOfLine(new RestOfConfigLine("", new ErrorContainer()));
	}

	public RestOfConfigLine getRestOfLine() {
		return restOfLine;
	}

	public void setRestOfLine(RestOfConfigLine restOfLine) {
		this.restOfLine = restOfLine;
		this.data = restOfLine.getLeds();
		this.clickEditable = restOfLine.getClickEditableColumns();
	}

	public boolean isColumnEditable(int col) {
		if (col > clickEditable.length)
			return false;
		return clickEditable[col];
	}

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.MIXED;
	}

	@Override
	public String getRawStringForRestOfLine() {
		return this.restOfLine.getModifiedRawString();
	}

	@Override
	public String toString() {
		return getAnimationType().getDescription() + " "
				+ restOfLine.getModifiedRawString();
	}

	@Override
	public int getNumBytes() {
		return 1 // modus byte
		+ restOfLine.getNumBytes() //
		+ 1; // for end of line delimiter
	}

}
