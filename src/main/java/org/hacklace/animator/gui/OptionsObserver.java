package org.hacklace.animator.gui;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Speed;

public interface OptionsObserver {
	public void onSpeedChanged(Speed newSpeed);
	public void onDelayChanged(Delay newDelay);
	public void onPositionChanged(int newPosition);
	public void onSaveAnimation();
}
