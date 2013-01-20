/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui.actions;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.enums.StepWidth;
import org.hacklace.animator.gui.actions.StepWidthObserver;

public interface OptionsObserver extends StepWidthObserver {
	public void onSpeedChanged(Speed newSpeed);

	public void onDelayChanged(Delay newDelay);

	public void onDirectionChanged(Direction newDirection);

	@Override
	public void onStepChanged(StepWidth newStepWidth);
}
