package org.hacklace.animator.enums;

public enum StepWidth {
	ONE(0, 1), // mostly for text
	FIVE(1, 5); // mostly for graphics

	private int bit;
	private int value;
	
	private StepWidth(int bit, int stepWidth) {
		this.bit = bit;
		this.value = stepWidth;
	}

	public int getBit() {
		return bit;
	}

	public int getValue() {
		return value;
	}
	
	/**
	 * 76543210
	 * bit 3 is set (or not set)
	 * @return
	 */
	public byte getStepWidthAsByte() {
		return (byte) (this.bit >> 3);
	}
	
	/**
	 * 
	 * @param bit 0 or 1
	 * @return
	 */
	public static StepWidth fromBit(int bit) {
		if (bit == 0) { 
			return ONE;
		}
		if (bit == 1) {
			return FIVE;
		}
		throw new IllegalArgumentException("Bit must be 0 or 1, is "+bit);
	}
	
	public static StepWidth fromBoolean(boolean bit) {
		if (bit) {
			return FIVE;
		}
		return ONE;
	}
	 	
}
