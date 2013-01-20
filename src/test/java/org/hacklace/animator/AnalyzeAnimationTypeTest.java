/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.configuration.FullConfigLine;

public class AnalyzeAnimationTypeTest extends TestCase {
	
	public void testAnalyze() {
		FullConfigLine fullLine = new FullConfigLine("$0C,^A");
		String newRawString = fullLine.getRestOfLine(new ErrorContainer())
				.getModifiedRawString();
		assertEquals("^A", newRawString);
	}
	
//	public void testAnalyzeAnimationType() {
//		FullConfigLine fullLine = new FullConfigLine(
//				"$0C,$FF $55 $2A $55 $2A $55 $2A $55 $2A $55 $2A $FF,");
//		AnimationType animationType = fullLine.getRestOfLine(
//				new ErrorContainer()).analyzeType();
//		assertEquals(AnimationType.GRAPHIC, animationType);
//	}



}
