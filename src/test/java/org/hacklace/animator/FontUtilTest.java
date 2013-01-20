/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator;

import junit.framework.TestCase;


public class FontUtilTest extends TestCase{
	public void testIValidHacklaceChar() {
		ErrorContainer err = new ErrorContainer();
		assertTrue(FontUtil.isValidHacklaceChar('a', err));
		assertTrue(FontUtil.isValidHacklaceChar('A', err));
		assertTrue(FontUtil.isValidHacklaceChar('1', err));
		assertTrue(FontUtil.isValidHacklaceChar('0', err));
		assertTrue(FontUtil.isValidHacklaceChar('€', err));
		assertFalse(FontUtil.isValidHacklaceChar('à', err));
	}
}
