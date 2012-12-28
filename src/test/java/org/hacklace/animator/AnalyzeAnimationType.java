package org.hacklace.animator;

import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.enums.AnimationType;

import junit.framework.TestCase;

public class AnalyzeAnimationType extends TestCase {
  public void testAnalyzeAnimationType() {
	  String fullLine = "$0C,$FF $55 $2A $55 $2A $55 $2A $55 $2A $55 $2A $FF,";
	  AnimationType animationType = DisplayBuffer.fullLineAnalyzeType(fullLine);
	  assertEquals(AnimationType.GRAPHIC, animationType);
  }
}
