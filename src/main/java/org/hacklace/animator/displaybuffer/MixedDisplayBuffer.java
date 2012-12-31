package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.IllegalHacklaceConfigLineException;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.enums.AnimationType;

public class MixedDisplayBuffer extends DisplayBuffer {

	@Override
	public String toString() {
		return getAnimationType().getDescription() + " " + stringValue;
	}

	protected String stringValue;

	public MixedDisplayBuffer(FullConfigLine fullLine)
			throws IllegalHacklaceConfigLineException {
		super(fullLine.getModusByte());
		this.stringValue = fullLine.getRestOfLine().getValue();
	}

	public MixedDisplayBuffer() {
		this.stringValue = "";
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.MIXED;
	}

	@Override
	public String getRawStringForRestOfLine() {
		return getStringValue();
	}

}
