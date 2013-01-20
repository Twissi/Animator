/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator;

import junit.framework.TestCase;

import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.configuration.RestOfConfigLine;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;

public class AnalyzeAnimationSizeTest extends TestCase {

	public void testLengthWithFinalZeros() {
		FullConfigLine fullLine = new FullConfigLine("$04,$FF,$01,$00,$FF,");
		RestOfConfigLine restOfLine = fullLine.getRestOfLine(new ErrorContainer());
		int numColumns = restOfLine.getNumColumns();
		assertEquals(2, numColumns);
	}

	public void testLengthWithFinalZerosViaBuffer() {
		FullConfigLine fullLine = new FullConfigLine("$04,$FF,$01,$00,$FF,");
		ErrorContainer errorContainer = new ErrorContainer();

		GraphicDisplayBuffer buffer = (GraphicDisplayBuffer) DisplayBuffer
				.createBufferFromLine(fullLine, errorContainer);
		assertTrue(errorContainer.isFreeOfErrorsAndWarnings());
		int numColums2 = buffer.getNumColumns();
		assertEquals(2, numColums2); 

		assertEquals("$01,$00,", buffer.getRawStringForDirectMode()); 
		
	}

}
