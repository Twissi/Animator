package org.hacklace.animator;

import org.hacklace.animator.configuration.RestOfConfigLine;
import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;

public class ElementTest extends TestCase {

	@Test
	@Ignore
	public void test() {
		try {
			new RestOfConfigLine(null, null); // throws assertion error
			// you shouldn't get here if you have assert enabled
			assertTrue(false);
			// How to enable assert statements in Eclipse:
			// Window > Preferences > Java > Installed JREs
			// select the JRE (Java 6) and click Edit
			// add the VM argument -enableassertions
		} catch (AssertionError ex) {
			assertTrue(true);
		}
		
	}
}
