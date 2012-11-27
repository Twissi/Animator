package org.hacklace.animator.gui;

import java.awt.Color;

import javax.swing.JButton;

public class GridButton extends JButton {
	
	private static final long serialVersionUID = -5214373326373158893L;
	
	public final int row;
	public final int column;
	protected boolean pressed;
	
	public void set() {
		pressed = true;
		setBackground(Color.BLACK);
	}
	
	public void unset() {
		pressed = false;
		setBackground(Color.WHITE);
	}
	
	public GridButton(int row, int column) {
		setOpaque(true);
		setBackground(Color.WHITE);
		this.row = row;
		this.column = column;
	}

}
