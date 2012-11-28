package org.hacklace.animator.enums;

import java.security.InvalidParameterException;

public enum Speed {
	ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7);

	public static Speed fromInt(int speed) {
		switch (speed) {
		case 0:
			return ZERO;
		case 1:
			return ONE;
		case 2:
			return TWO;
		case 3:
			return THREE;
		case 4:
			return FOUR;
		case 5:
			return FIVE;
		case 6:
			return SIX;
		case 7:
			return SEVEN;
		default:
			throw new InvalidParameterException(
					"There is no Speed assigned to value: " + speed);
		}
	}

	public final int value;

	private Speed(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
