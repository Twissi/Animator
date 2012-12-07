package org.hacklace.animator.gui;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Speed;

public interface OptionsObserver {
	public abstract void onSpeedChanged(Speed newSpeed);
	public abstract void onDelayChanged(Delay newDelay);
}
