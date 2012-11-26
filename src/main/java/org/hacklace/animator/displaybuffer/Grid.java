package org.hacklace.animator.displaybuffer;

public class Grid {
	
	private boolean[][] data;
	private final int rows;
	private final int columns;
	
	public Grid(int rows, int columns) {
		data = new boolean[columns][rows];
		this.rows = rows;
		this.columns = columns;
	}

	public boolean[][] getData() {
		return data;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++ ) {
				sb.append( data[column][row] ? "1" : "- ");
			}
			sb.append("\n" );
		}
		return sb.toString();
	}


}
