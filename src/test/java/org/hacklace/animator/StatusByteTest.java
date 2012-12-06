package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.enums.StepWidth;

public class StatusByteTest extends TestCase {
	public void testStatus() {
		Direction inDirection = Direction.BIDIRECTIONAL;
		Delay inDelay = Delay.FOUR;
		StepWidth inStepWidth = StepWidth.FIVE;
		Speed inSpeed = Speed.SIX;
		StatusByte statusByte = new StatusByte(inDirection, inDelay,
				inStepWidth, inSpeed);
		assertEquals(Direction.BIDIRECTIONAL, statusByte.getDirection());
		assertEquals(Delay.FOUR, statusByte.getDelay());
		assertEquals(StepWidth.FIVE, statusByte.getStepWidth());
		assertEquals(Speed.SIX, statusByte.getSpeed());
	}
}
