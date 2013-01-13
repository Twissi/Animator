package org.hacklace.animator.gui.actions;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hacklace.animator.enums.Delay;

public class DelayChangeListener implements ChangeListener {

	private final OptionsObserver optionsObserver;
	private JSlider delaySlider;
	
	public DelayChangeListener(OptionsObserver optionsObserver, JSlider delaySlider) {
		this.optionsObserver = optionsObserver;
		this.delaySlider = delaySlider;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
			int intDelay = delaySlider.getValue();
			optionsObserver.onDelayChanged(Delay.fromInt(intDelay));

		
	}
}
