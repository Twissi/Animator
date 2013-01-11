package org.hacklace.animator.gui.actions;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.gui.EditPanel;

public class SpeedChangeListener implements ChangeListener {

	private final EditPanel editPanel;
	private JSlider speedSlider;

	public SpeedChangeListener(EditPanel editPanel, JSlider speedSlider) {
		this.editPanel = editPanel;
		this.speedSlider = speedSlider;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		int intSpeed = speedSlider.getValue();
		editPanel.onSpeedChanged(Speed.fromInt(intSpeed));
	}
}
