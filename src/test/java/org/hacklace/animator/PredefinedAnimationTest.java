/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
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
