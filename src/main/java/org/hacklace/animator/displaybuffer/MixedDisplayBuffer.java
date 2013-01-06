package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.configuration.RestOfConfigLine;
import org.hacklace.animator.enums.AnimationType;

public class MixedDisplayBuffer extends DisplayBuffer {

	private RestOfConfigLine restOfLine;
	private boolean[] clickEditable;

	public MixedDisplayBuffer(FullConfigLine fullLine,
			ErrorContainer errorContainer) {
		super(fullLine.getModusByte(errorContainer));
		setRestOfLine(fullLine.getRestOfLine(), errorContainer);
	}

	public MixedDisplayBuffer() {
		setRestOfLine(new RestOfConfigLine(""), new ErrorContainer());
	}

	public RestOfConfigLine getRestOfLine() {
		return restOfLine;
	}

	public void setRestOfLine(RestOfConfigLine restOfLine,
			ErrorContainer errorContainer) {
		this.restOfLine = restOfLine;
		this.data = restOfLine.getLeds(errorContainer);
		this.clickEditable = restOfLine.getClickEditableColumns(errorContainer);
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
		return this.restOfLine.getValue();
	}

	@Override
	public String toString() {
		return getAnimationType().getDescription() + " "
				+ restOfLine.getValue();
	}

	@Override
	public int getNumBytes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countUsedColumns() {
		// the number of used columns in a Mixed animation is determined how?
		// TODO xx
		return 0;
	}
}
