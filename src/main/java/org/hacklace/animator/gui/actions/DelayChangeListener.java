package org.hacklace.animator.gui.actions;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.gui.EditPanel;

public class DelayChangeListener implements ChangeListener {

	private final EditPanel editPanel;
	private JSlider delaySlider;
	
	public DelayChangeListener(EditPanel editPanel, JSlider delaySlider) {
		this.editPanel = editPanel;
		this.delaySlider = delaySlider;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
			int intDelay = delaySlider.getValue();
			editPanel.onDelayChanged(Delay.fromInt(intDelay));

		
	}
}
