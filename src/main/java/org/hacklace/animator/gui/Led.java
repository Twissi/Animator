package org.hacklace.animator.gui;

import java.awt.Color;

import javax.swing.JButton;

public class Led extends JButton implements LedInterface {

	private static final long serialVersionUID = -5214373326373158893L;

	public final int row;
	public final int column;
	protected boolean on;

	public void set() {
		on = true;
		setBackground(Color.BLACK);
	}

	public void unset() {
		on = false;
		setBackground(Color.WHITE);
	}

	public void toggle() {
		if (on) {
			unset();
		} else {
			set();
		}
	}
	
	public boolean isOn() {
		return on;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}

	public Led(int row, int column) {
		setOpaque(true);
		setBackground(Color.WHITE);
		this.row = row;
		this.column = column;
	}
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (!enabled) {
			setBackground(new Color(128, 128, 128, 128));
		}
	}
	

}
