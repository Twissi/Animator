package org.hacklace.animator.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
				b.addActionListener( new ToggleActionListener(b));
				buttons[column][row] = b;
				add(b);
			}
		}
	}
	
	private class ToggleActionListener implements ActionListener {
		
		private GridButton button;
		private boolean pressed;
		
		private ToggleActionListener(GridButton button) {
			this.button = button;
			pressed = false;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			button.setOpaque(true);
			if( pressed ) {				
				button.setBackground( Color.WHITE);
			} else {				
				button.setBackground(Color.BLACK);
			}			
			
			pressed = !pressed;
			
			grid.data[button.column][button.row] = pressed;
			System.out.println(grid);
		}
		
	}
	



}