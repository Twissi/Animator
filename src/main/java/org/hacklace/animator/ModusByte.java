package org.hacklace.animator;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.enums.StepWidth;

public class ModusByte implements Cloneable {
	private byte bits;

	public ModusByte() {

	}

	public ModusByte(byte b) {
		this.bits = b;
	}

	public ModusByte(Direction direction, Delay delay, StepWidth stepWidth,
			Speed speed) {
		byte b = (byte) (direction.getValue() << 7);
		b += (delay.getValue() << 4);
		b += (stepWidth.getBit() << 3);
		b += (speed.getValue());
		this.bits = b;
	}

	/**
	 * 
	 * @param index
	 *            from 7 to 0
	 */
	protected boolean isBitSet(int index) {
		byte mask = (byte) Math.pow(2, index);
		byte result = (byte) (mask & bits);
		if (result == 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isEOF() {
		return (this.bits == 0);
	}

	public Direction getDirection() {
		if (isBitSet(7)) {
			return Direction.BIDIRECTIONAL;
		} else {
			return Direction.FORWARD;
		}
	}

	public StepWidth getStepWidth() {
		return StepWidth.fromBoolean(isBitSet(3));
	}

	public Speed getSpeed() {
		int bitZero = (isBitSet(0) ? 1 : 0);
		int bitOne = (isBitSet(1) ? 2 : 0);
		int bitTwo = (isBitSet(2) ? 4 : 0);
		int speed = bitZero + bitOne + bitTwo;
		return Speed.fromInt(speed);
	}

	public Delay getDelay() {
		int bitZero = (isBitSet(4) ? 1 : 0);
		int bitOne = (isBitSet(5) ? 2 : 0);
		int bitTwo = (isBitSet(6) ? 4 : 0);
		int delay = bitZero + bitOne + bitTwo;
		return Delay.fromInt(delay);
	}

	public byte getByte() {
		return this.bits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bits;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModusByte other = (ModusByte) obj;
		if (bits != other.bits)
			return false;
		return true;
	}

	public void setDirection(Direction direction) {
		int bit7 = direction.getValue() << 7;
		// clear bit 7
		this.bits &= 0x7F; // 0111 1111
		// set first bit (bit 7) to the desired value, leave rest unchanged
		this.bits |= bit7;
	}

	public void setDelay(Delay delay) {
		int bit654 = delay.getValue() << 4;
		// clear bits
		this.bits &= 0x8F; // 1000 1111
		// set bits 654 to the desired value, leave rest unchanged
		this.bits |= bit654;
	}

	public void setStepWidth(StepWidth stepWidth) {
		int bit3 = stepWidth.getBit() << 3;
		// clear bit
		this.bits &= 0xF7; // 1111 0111
		// set bit 3 to the desired value, leave rest unchanged
		this.bits |= bit3;
	}

	public void setSpeed(Speed speed) {
		// clear bits 0-2
		this.bits &= 0xF8; // 1111 1000
		// set bits 0-2
		this.bits |= speed.value;
	}

	public ModusByte clone() {
		ModusByte copy = new ModusByte();
		copy.bits = this.bits;
		return copy;
	}

	public ModusByte(String statusByteString, int line)
			throws IllegalHacklaceConfigFileException {
		if (!ConversionUtil.isHexSequence(statusByteString)) {
			throw new IllegalHacklaceConfigFileException("Status string "
					+ statusByteString + " is not hex ($nn) in line " + line + ".");
		}
		this.bits = ConversionUtil.convertStringToByte(statusByteString);
	}

	public ModusByte(String statusByteString) {
		this.bits = ConversionUtil.convertStringToByte(statusByteString);
	}

}
