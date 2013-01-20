/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui.actions;

import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;
import org.hacklace.animator.gui.EditGraphicPanel;

public class CopyAndReplaceFrameActionListener extends AbstractFrameActionListener {

	public CopyAndReplaceFrameActionListener(EditGraphicPanel editGraphicPanel) {
		super(editGraphicPanel);
	}

	@Override
	protected void specificAction(GraphicDisplayBuffer buffer, int currentFramePos) {
		buffer.copyAndReplaceFrame(currentFramePos);
	}

}
