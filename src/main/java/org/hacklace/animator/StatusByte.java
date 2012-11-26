package org.hacklace.animator;

import org.hacklace.animator.enums.Direction;

public class StatusByte {
	private byte bits;

	public StatusByte() {

	}

	public StatusByte(byte b) {
		this.bits = b;
	}

	/**
	 * 
	 * @param index
	 *            from 7 to 0
	 */
	public boolean isBitSet(int index) {
		byte mask = (byte) Math.pow(2, index);
		byte result = (byte) (mask | bits);
		if (result == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public Direction getDirection()
	{
		if (isBitSet(8)) {
			return Direction.BIDIRECTIONAL;
		}
		else
		{
			return Direction.FORWARD;
		}
	}
	
	

}
