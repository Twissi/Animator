package org.hacklace.animator.enums;

public enum AnimationType {
	GRAPHIC(1), TEXT(0);

	public final int value;

	private AnimationType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
