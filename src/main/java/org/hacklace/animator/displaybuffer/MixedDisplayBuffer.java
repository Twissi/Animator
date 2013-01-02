package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.IllegalHacklaceConfigLineException;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.configuration.RestOfConfigLine;
import org.hacklace.animator.enums.AnimationType;

public class MixedDisplayBuffer extends DisplayBuffer {

	private RestOfConfigLine restOfLine;
	private boolean[] clickEditable;

	public MixedDisplayBuffer(FullConfigLine fullLine)
			throws IllegalHacklaceConfigLineException {
		super(fullLine.getModusByte());
		setRestOfLine(fullLine.getRestOfLine());
	}

	public MixedDisplayBuffer() {
		setRestOfLine(new RestOfConfigLine(""));
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

}
