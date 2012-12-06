package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.enums.AnimationType;

public class MixedDisplayBuffer extends DisplayBuffer {

	protected String stringValue;

	public MixedDisplayBuffer(String value) {
		this.stringValue = value;
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

}
