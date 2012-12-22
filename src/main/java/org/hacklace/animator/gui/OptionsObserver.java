package org.hacklace.animator.gui;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;

public interface OptionsObserver {
	public void onSpeedChanged(Speed newSpeed);
	public void onDelayChanged(Delay newDelay);
	public void onDirectionChanged(Direction newDirection);
	public void onSaveAnimation();
}
