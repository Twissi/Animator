package org.hacklace.animator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.hacklace.animator.displaybuffer.Grid;

public class ToggleActionListener implements ActionListener {

	private GridButton button;
	private Grid grid;

	public ToggleActionListener(Grid grid, GridButton button) {
		this.button = button;
		this.grid = grid;
		button.pressed = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		button.setOpaque(true);
		if (button.pressed) {
			button.unset();
		} else {
			button.set();
		}
		grid.data[button.column][button.row] = button.pressed;

	}

}
