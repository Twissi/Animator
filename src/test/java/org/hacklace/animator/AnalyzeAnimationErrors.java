package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.MixedDisplayBuffer;
import org.hacklace.animator.enums.AnimationType;
import org.junit.Test;

public class AnalyzeAnimationErrors extends TestCase {

	@Test
	public void missingKomma1() {
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
	
	public static void main(String[] args) {
		AnalyzeAnimationErrors it = new AnalyzeAnimationErrors();
		it.missingKomma1();
	}

}
