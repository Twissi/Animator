package org.hacklace.animator.gui.actions;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hacklace.animator.enums.Speed;

public class SpeedChangeListener implements ChangeListener {

	private final JSlider speedSlider;
	private final List<OptionsObserver> optionsObserverList;

	public SpeedChangeListener(JSlider speedSlider,
			OptionsObserver optionsObserver) {
		this.speedSlider = speedSlider;
		this.optionsObserverList = new LinkedList<OptionsObserver>();
		addObserver(optionsObserver);
	}

	public void addObserver(OptionsObserver optionsObserver) {
		optionsObserverList.add(optionsObserver);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		int intSpeed = speedSlider.getValue();
		// currently there is only one observer, the EditPanel
		for (OptionsObserver o : optionsObserverList) {
			o.onSpeedChanged(Speed.fromInt(intSpeed));
		}
	}
}
