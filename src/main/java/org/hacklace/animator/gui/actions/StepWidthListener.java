package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.hacklace.animator.enums.StepWidth;
import org.hacklace.animator.gui.EditPanel;

public class StepWidthListener implements ActionListener {

	private final EditPanel editPanel;
	private StepWidth stepWidth;

	public StepWidthListener(EditPanel editPanel, StepWidth stepWidth) {
		this.editPanel = editPanel;
		this.stepWidth = stepWidth;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		editPanel.onStepChanged(stepWidth);
	}
}
