package org.hacklace.animator;

import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;

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
		byte result = (byte) (mask & bits);
		if (result == 0) {
			return false;
		} else {
			return true;
		}
	}

	public Direction getDirection() {
		if (isBitSet(7)) {
			return Direction.BIDIRECTIONAL;
		} else {
			return Direction.FORWARD;
		}
	}

	public AnimationType getAnimationType() {
		if (isBitSet(3)) {
			return AnimationType.GRAPHIC;
		}else{
			return AnimationType.TEXT;
		}
	}
	
	public Speed getSpeed() {
		int bitZero = (isBitSet(0) ? 1 : 0);
		int bitOne = (isBitSet(1) ? 2 : 0);
		int bitTwo = (isBitSet(2) ? 4 : 0);
		int speed = bitZero+bitOne+bitTwo;
		return Speed.fromInt(speed);
	}

	public Delay getDelay() {
		int bitZero = (isBitSet(4) ? 1 : 0);
		int bitOne = (isBitSet(5) ? 2 : 0);
		int bitTwo = (isBitSet(6) ? 4 : 0);
		int delay = bitZero+bitOne+bitTwo;
		return Delay.fromInt(delay);
	}
}
