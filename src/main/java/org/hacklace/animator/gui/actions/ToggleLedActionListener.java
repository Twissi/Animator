package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.hacklace.animator.gui.LedInterface;

public class ToggleLedActionListener implements ActionListener {

	private LedInterface led;

	public ToggleLedActionListener(LedInterface button) {
		this.led = button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (led.isOn()) {
			led.unset();
		} else {
			led.set();
		}
	}

}
