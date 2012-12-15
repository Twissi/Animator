package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.displaybuffer.FontUtil;

public class FontUtilTest extends TestCase{
	public void testIValidHacklaceChar() {
		assertTrue(FontUtil.isValidHacklaceChar('a'));
		assertTrue(FontUtil.isValidHacklaceChar('A'));
		assertTrue(FontUtil.isValidHacklaceChar('1'));
		assertTrue(FontUtil.isValidHacklaceChar('0'));
		assertTrue(FontUtil.isValidHacklaceChar('€'));
		assertFalse(FontUtil.isValidHacklaceChar('à'));
	}
}
