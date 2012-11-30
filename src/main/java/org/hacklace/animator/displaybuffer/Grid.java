package org.hacklace.animator.displaybuffer;

import java.util.Arrays;

public class Grid {

	private boolean[][] data;
	private final int rows;
	private final int columns;

	public Grid() {
		this(DisplayBuffer.ROWS, DisplayBuffer.COLUMNS); // default 7, 5
	}

	public Grid(int rows, int columns) {
		data = new boolean[columns][rows];
		this.rows = rows;
		this.columns = columns;
	}

//	public boolean[][] getData() {
//		return data;
//	}
	
	public void setColumnRow(int column, int row, boolean status) {
		data[column][row] = status;
	}
	
	public boolean getColumnRow(int column, int row) {
		return data[column][row];
	}

	public void setDataFromBytes(int[] aniBytes) {
		for (int column = 0; column < columns; column++) {
			byte mask = 1;

			for (int i = 0; i < rows; i++) {
				byte maskResult = (byte) (mask & aniBytes[column]);

				this.data[column][i] = ((maskResult != 0) ? true : false);
				mask <<= 1;

			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				sb.append(data[column][row] ? "1 " : "- ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columns;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + rows;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grid other = (Grid) obj;
		if (columns != other.columns)
			return false;
		if (rows != other.rows)
			return false;
		if (!Arrays.deepEquals(data, other.data))
			return false;

		return true;
	}

}