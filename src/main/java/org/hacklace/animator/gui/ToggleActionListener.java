package org.hacklace.animator.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.hacklace.animator.displaybuffer.Grid;

public class ToggleActionListener implements ActionListener {

	private GridButton button;
	private Grid grid;
	private boolean pressed;
	

	private ToggleActionListener(Grid grid, GridButton button) {
		this.button = button;
		this.grid = grid;
		pressed = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		button.setOpaque(true);
		if (pressed) {
			button.setBackground(Color.WHITE);
		} else {
			button.setBackground(Color.BLACK);
		}

		pressed = !pressed;

		grid.data[button.column][button.row] = pressed;
		System.out.println(grid);
	}

}
