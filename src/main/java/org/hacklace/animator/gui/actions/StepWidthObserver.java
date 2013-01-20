/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui.actions;

import org.hacklace.animator.enums.StepWidth;

public interface StepWidthObserver {
	public void onStepChanged(StepWidth newStepWidth);
}
