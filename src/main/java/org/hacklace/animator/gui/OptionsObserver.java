package org.hacklace.animator.gui;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.enums.StepWidth;

public interface OptionsObserver {
	public void onSpeedChanged(Speed newSpeed);
	public void onDelayChanged(Delay newDelay);
	public void onDirectionChanged(Direction newDirection);
	public void onStepChanged(StepWidth newStep);
	public boolean onSaveAnimation();
}
