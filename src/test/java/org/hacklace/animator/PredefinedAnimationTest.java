package org.hacklace.animator;

import static org.junit.Assert.*;

import org.hacklace.animator.enums.PredefinedAnimation;
import org.junit.Test;

public class PredefinedAnimationTest {

	@Test
	public void testPredefinedAnimation() {
		PredefinedAnimation animation = PredefinedAnimation.getPredefinedAnimationByIndex('U');
		assertEquals(animation.getIndex(),'U');
	}

}
