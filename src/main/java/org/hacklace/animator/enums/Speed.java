package org.hacklace.animator.enums;

import java.security.InvalidParameterException;

public enum Speed {
	ZERO,
	ONE,
	TWO,
	THREE,
	FOUR,
	FIVE,
	SIX,
	SEVEN;
	
	public static Speed fromInt(int speed){
		switch(speed){
			case 0: return ZERO;
			case 1: return ONE;
			case 2: return TWO;
			case 3: return THREE;
			case 4: return FOUR;
			case 5: return FIVE;
			case 6: return SIX;
			case 7: return SEVEN;
			default: throw new InvalidParameterException("There is no Speed assigned to value: " + speed);
		}
	}
}


