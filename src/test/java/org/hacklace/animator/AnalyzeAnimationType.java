package org.hacklace.animator;

import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.enums.AnimationType;

import junit.framework.TestCase;

public class AnalyzeAnimationType extends TestCase {
  public void testAnalyzeAnimationType() {
	  FullConfigLine fullLine = new FullConfigLine("$0C,$FF $55 $2A $55 $2A $55 $2A $55 $2A $55 $2A $FF,");
	  AnimationType animationType = fullLine.analyzeType();
	  assertEquals(AnimationType.GRAPHIC, animationType);
  }
}
