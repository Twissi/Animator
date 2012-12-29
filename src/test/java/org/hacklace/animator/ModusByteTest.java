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
	
	public void testSetterGetter() {
		ModusByte modusByte = new ModusByte();
		assertEquals("Modusbyte default has changed from 4", 4, modusByte.getByte());
		// test direction
		assertEquals(Direction.FORWARD, modusByte.getDirection());
		modusByte.setDirection(Direction.BIDIRECTIONAL);
		assertEquals("set-/getDirection failed", Direction.BIDIRECTIONAL, modusByte.getDirection());
		// test delay
		assertEquals(Delay.ZERO, modusByte.getDelay());
		modusByte.setDelay(Delay.THREE);
		assertEquals("set-/getDelay failed", Delay.THREE, modusByte.getDelay());
		// test stepWidth
		assertEquals(StepWidth.ONE, modusByte.getStepWidth());
		modusByte.setStepWidth(StepWidth.FIVE);
		assertEquals(StepWidth.FIVE, modusByte.getStepWidth());
		// test speed
		assertEquals(Speed.FOUR, modusByte.getSpeed());
		modusByte.setSpeed(Speed.FIVE);
		assertEquals(Speed.FIVE, modusByte.getSpeed());
	}
}
