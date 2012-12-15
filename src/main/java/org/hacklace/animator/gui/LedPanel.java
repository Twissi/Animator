package org.hacklace.animator.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.Grid;

public class LedPanel extends JPanel {

	private static final long serialVersionUID = 3966890869284361505L;

	private final int ROWS;
	private final int COLUMNS;

	private Led[][] buttons;
	private Grid grid;

	public LedPanel(int rows, int columns) {
		this.ROWS = rows;
		this.COLUMNS = columns;

		buttons = new Led[COLUMNS][ROWS];
		grid = new Grid(ROWS, COLUMNS);

		initComponents();
		setVisible(true);
	}

	public LedPanel() {
		this(DisplayBuffer.ROWS, DisplayBuffer.COLUMNS);
	}

	public void setLed(int row, int column, boolean val) {
		if (val) {
			buttons[column][row].set();
		} else {
			buttons[column][row].unset();
		}
	}

	private void initComponents() {
		GridLayout layout = new GridLayout(ROWS, COLUMNS);
		setLayout(layout);

		for (int row = 0; row < ROWS; row++) {
			for (int column = 0; column < COLUMNS; column++) {
				Led b = new Led(row, column);
				b.addActionListener(new ToggleLedActionListener(grid, b));
				buttons[column][row] = b;
				add(b);
			}
		}
	}
	
	public void clear() {
		for (int x = 0; x < DisplayBuffer.COLUMNS; x++) {
			for (int y = 0; y < DisplayBuffer.ROWS; y++) {
				setLed(y, x, false);
			}
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (int x = 0; x < DisplayBuffer.COLUMNS; x++) {
			for (int y = 0; y < DisplayBuffer.ROWS; y++) {
				buttons[x][y].setEnabled(enabled);
			}
		}
	}

//	public static void main(String[] args) throws InterruptedException {
//		JFrame f = new JFrame("Test");
//		LedPanel p = new LedPanel(7, 5);
//		f.add(p);
//		f.pack();
//		f.setSize(500, 700);
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		f.setVisible(true);
//		for (int i = FontUtil.LOWEST_INDEX; i < FontUtil.HIGHEST_INDEX + 1; i++) {
//			p.grid.setDataFromBytes(FontUtil.getFiveBytesForIndex(i));
//			for (int row = 0; row < p.ROWS; row++) {
//				for (int column = 0; column < p.COLUMNS; column++) {
//					p.setLed(row, column, p.grid.getColumnRow(column,row));
//				}
//
//			}
//			Thread.sleep(1000);
//
//		}
//
//	}
}