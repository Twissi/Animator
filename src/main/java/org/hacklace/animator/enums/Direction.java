package org.hacklace.animator.enums;

public enum Direction {
	FORWARD (0),
	BIDIRECTIONAL (1);
	
	public final int value;
	Direction(int value) {
		this.value = value;
	}
	public int getValue() {
		return this.value;
	}
}
