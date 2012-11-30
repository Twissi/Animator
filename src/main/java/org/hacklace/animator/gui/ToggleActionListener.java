package org.hacklace.animator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.hacklace.animator.displaybuffer.Grid;

public class ToggleActionListener implements ActionListener {

	private LedInterface led;
	private Grid grid;

	public ToggleActionListener(Grid grid, LedInterface button) {
		this.led = button;
		this.grid = grid;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (led.isOn()) {
			led.unset();
		} else {
			led.set();
		}
		grid.setColumnRow(led.getColumn(), led.getRow(), led.isOn());

	}

}
