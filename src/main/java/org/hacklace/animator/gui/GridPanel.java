package org.hacklace.animator.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
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
	
	public void setPixel(int row, int column, boolean val) {
		if( val) {
			buttons[column][row].set();
		} else {
			buttons[column][row].unset();
		}		
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
	
	public static void main(String[] args) throws InterruptedException {
		JFrame f = new JFrame("Test");
		GridPanel p = new GridPanel(7,5 );
		f.add(p);
		f.pack();
		f.setSize(500, 700);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		for( int i = 0; i < 130; i++) {
			p.grid.setDataFromBytes( FontUtil.repr(i));
			for(int row = 0; row < p.ROWS; row++) {
				for(int column = 0; column < p.COLUMNS; column++ ) {
					p.setPixel(row, column, p.grid.data[column][row]);
				}
				
			}
			Thread.sleep(1000);
			
		}
		
		

	}
}