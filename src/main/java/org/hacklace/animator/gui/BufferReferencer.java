/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.displaybuffer.DisplayBuffer;

public interface BufferReferencer {
	public DisplayBuffer getBuffer();
	public void showErrors(ErrorContainer errorContainer);
}
