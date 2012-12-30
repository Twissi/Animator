package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.hacklace.animator.displaybuffer.Grid;
import org.hacklace.animator.gui.LedInterface;

public class ToggleLedActionListener implements ActionListener {

	private LedInterface led;
	private Grid grid;

	public ToggleLedActionListener(Grid grid, LedInterface button) {
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
