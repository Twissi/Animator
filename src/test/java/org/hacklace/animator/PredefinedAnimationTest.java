package org.hacklace.animator;

import static org.junit.Assert.assertEquals;

import org.hacklace.animator.enums.PredefinedAnimation;

public class PredefinedAnimationTest {

	public void testPredefinedAnimation() {
		ErrorContainer errorContainer = new ErrorContainer();
		PredefinedAnimation animation = PredefinedAnimation.getPredefinedAnimationByIndex('U', errorContainer);
		assertEquals(animation.getIndex(),'U');
		assert(errorContainer.isFreeOfErrorsAndWarnings());
	}

}
