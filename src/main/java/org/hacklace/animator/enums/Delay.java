/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.enums;

import java.security.InvalidParameterException;

public enum Delay {
	ZERO(0),
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7);
	
	public final int value;
	private Delay(int value) {
		this.value = value;
	}
	
	public static Delay fromInt(int delay){
		switch(delay){
			case 0: return ZERO;
			case 1: return ONE;
			case 2: return TWO;
			case 3: return THREE;
			case 4: return FOUR;
			case 5: return FIVE;
			case 6: return SIX;
			case 7: return SEVEN;
			default: throw new InvalidParameterException("There is no Delay assigned to value: " + delay);
		}
	}

	public int getValue() {
		return this.value;
	}
}
