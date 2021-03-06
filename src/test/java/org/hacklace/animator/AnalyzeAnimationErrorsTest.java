/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.MixedDisplayBuffer;
import org.hacklace.animator.enums.AnimationType;

public class AnalyzeAnimationErrorsTest extends TestCase {

	public void testMissingKomma1() {
		String rawString = "$04,~A$80";
		FullConfigLine fullLine = new FullConfigLine(rawString);
		ErrorContainer errorContainer = new ErrorContainer();
		MixedDisplayBuffer buffer = (MixedDisplayBuffer) DisplayBuffer.createBufferFromLine(fullLine,
				errorContainer);
		assertEquals(AnimationType.MIXED, buffer.getAnimationType());
		System.out.println(buffer.getRawStringForRestOfLine());
		String newRawString = buffer.getFullConfigLine().getRestOfLine(errorContainer).getModifiedRawString();
		System.out.println(errorContainer);
		assertEquals("~A$80,", newRawString);
	}
	

}
