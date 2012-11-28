package org.hacklace.animator;

import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;

import junit.framework.TestCase;

public class StatusByteTest extends TestCase {
	public void testStatus() {
		Direction inDirection = Direction.BIDIRECTIONAL;
		Delay inDelay = Delay.FOUR;
		AnimationType inAnimationType = AnimationType.GRAPHIC;
		Speed inSpeed = Speed.SIX;
		StatusByte statusByte = new StatusByte(inDirection, inDelay,
				inAnimationType, inSpeed);
		assertEquals(Direction.BIDIRECTIONAL, statusByte.getDirection());
		assertEquals(Delay.FOUR, statusByte.getDelay());
		assertEquals(AnimationType.GRAPHIC, statusByte.getAnimationType());
		assertEquals(Speed.SIX, statusByte.getSpeed());
	}
}
