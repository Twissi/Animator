package org.hacklace.animator.gui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.hacklace.animator.IniConf;
import org.hacklace.animator.displaybuffer.Grid;

public class LedPanel extends JPanel implements LedObserver {

	private static final long serialVersionUID = 3966890869284361505L;

	private final int gridRows;
	private final int gridCols;

	private Led[][] buttons;
	private Grid grid;
	
	private List<LedObserver> observerList;

	public LedPanel(int rows, int columns) {
		this.gridRows = rows;
		this.gridCols = columns;

		buttons = new Led[gridCols][gridRows];
		grid = new Grid(gridRows, gridCols);
		observerList = new ArrayList<LedObserver>();

		initComponents();
		setVisible(true);
	}

	public LedPanel() {
		this(IniConf.getInstance().rows(), IniConf.getInstance().columns());
	}

	public void setLed(int row, int column, boolean val) {
		if (val) {
			buttons[column][row].set();
		} else {
			buttons[column][row].unset();
		}
		for (LedObserver o: observerList) {
			o.onLedChange(row, column, val);
		}
	}

	private void initComponents() {
		GridLayout layout = new GridLayout(gridRows, gridCols);
		setLayout(layout);

		for (int row = 0; row < gridRows; row++) {
			for (int column = 0; column < gridCols; column++) {
				Led b = new Led(row, column, this);
				b.addActionListener(new ToggleLedActionListener(grid, b));
				buttons[column][row] = b;
				add(b);
			}
		}
	}
	
	public void clear() {
		for (int x = 0; x < gridCols; x++) {
			for (int y = 0; y < gridRows; y++) {
				setLed(y, x, false);
			}
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (int x = 0; x < gridCols; x++) {
			for (int y = 0; y < gridRows; y++) {
				buttons[x][y].setEnabled(enabled);
			}
		}
	}
	
	public void addObserver(LedObserver o) {
		observerList.add(o);
	}
	
	/**
	 * This bubbles the change events from leds to the ledPanel's registered observers
	 */
	public void onLedChange(int row, int column, boolean newValue) {
		for (LedObserver o: observerList) {
			o.onLedChange(row, column, newValue);
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
//			for (int row = 0; row < p.gridRows; row++) {
//				for (int column = 0; column < p.gridCols; column++) {
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