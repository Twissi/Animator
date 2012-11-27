package org.hacklace.animator.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.hacklace.animator.displaybuffer.Grid;

public class GridPanel extends JPanel {	

	private static final long serialVersionUID = 3966890869284361505L;
	
	private final int ROWS;
	private final int COLUMNS;
	
	private GridButton[][] buttons;
	private Grid grid;
	
	public GridPanel(int rows, int columns) {
		this.ROWS = rows;
		this.COLUMNS = columns;
		
		buttons = new GridButton[COLUMNS][ROWS];
		grid = new Grid(ROWS, COLUMNS);
		
		initComponents();
		setVisible(true);
	}
	
	private void initComponents() {		
		GridLayout layout = new GridLayout(ROWS, COLUMNS);
		setLayout(layout);		
		
		for(int row = 0; row < ROWS; row++) {
			for(int column = 0; column < COLUMNS; column++ ) {
				GridButton b = new GridButton(row, column);
				b.addActionListener( new ToggleActionListener(grid, b));
				buttons[column][row] = b;
				add(b);
			}
		}
	}
	
	



}