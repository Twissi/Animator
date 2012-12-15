package org.hacklace.animator.gui;

import java.awt.Color;

import javax.swing.JButton;

public class Led extends JButton implements LedInterface {

	private static final long serialVersionUID = -5214373326373158893L;

	public final int row;
	public final int column;
	protected boolean on;
	private LedObserver observer;

	public void set() {
		on = true;
		setBackground(Color.BLACK);
		observer.onLedChange(row, column, true);
	}

	public void unset() {
		on = false;
		setBackground(Color.WHITE);
		observer.onLedChange(row, column, false);
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

	public Led(int row, int column, LedObserver o) {
		setOpaque(true);
		setBackground(Color.WHITE);
		this.row = row;
		this.column = column;
		this.observer = o;
	}

}
