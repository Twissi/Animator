package org.hacklace.animator.gui.actions;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.gui.OptionsObserver;

public class SpeedChangeListener implements ChangeListener {

	private final OptionsObserver optionsObserver;
	private JSlider speedSlider;

	public SpeedChangeListener(OptionsObserver optionsObserver, JSlider speedSlider) {
		this.optionsObserver = optionsObserver;
		this.speedSlider = speedSlider;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		int intSpeed = speedSlider.getValue();
		optionsObserver.onSpeedChanged(Speed.fromInt(intSpeed));
	}
}
