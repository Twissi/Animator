package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.hacklace.animator.enums.StepWidth;

public class StepWidthListener implements ActionListener {

	private final OptionsObserver optionsObserver;
	private StepWidth stepWidth;

	public StepWidthListener(OptionsObserver optionsObserver, StepWidth stepWidth) {
		this.optionsObserver = optionsObserver;
		this.stepWidth = stepWidth;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		optionsObserver.onStepChanged(stepWidth);
	}
}
