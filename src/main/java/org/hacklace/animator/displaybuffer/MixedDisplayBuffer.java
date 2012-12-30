package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.IllegalHacklaceConfigLineException;
import org.hacklace.animator.ModusByte;
import org.hacklace.animator.enums.AnimationType;

public class MixedDisplayBuffer extends DisplayBuffer {

	@Override
	public String toString() {
		return getAnimationType().getDescription() + " " + stringValue;
	}

	protected String stringValue;

	public MixedDisplayBuffer(ModusByte modusByte, String value)  throws IllegalHacklaceConfigLineException {
		super();
		this.modusByte = modusByte;
		this.stringValue = value;
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
	public String getRawString() {
		return this.modusByte.getRawString() + getStringValue();
	}

}
