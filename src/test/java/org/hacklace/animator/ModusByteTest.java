package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.enums.StepWidth;

public class ModusByteTest extends TestCase {
	public void testStatus() {
		Direction inDirection = Direction.BIDIRECTIONAL;
		Delay inDelay = Delay.FOUR;
		StepWidth inStepWidth = StepWidth.FIVE;
		Speed inSpeed = Speed.SIX;
		ModusByte modusByte = new ModusByte(inDirection, inDelay,
				inStepWidth, inSpeed);
		assertEquals(Direction.BIDIRECTIONAL, modusByte.getDirection());
		assertEquals(Delay.FOUR, modusByte.getDelay());
		assertEquals(StepWidth.FIVE, modusByte.getStepWidth());
		assertEquals(Speed.SIX, modusByte.getSpeed());
	}
}
