package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.ConversionUtil.convertAnimationByteTo7Booleans;

import org.hacklace.animator.enums.AnimationType;

public class GraphicDisplayBuffer extends DisplayBuffer {

	public GraphicDisplayBuffer() {
		super();
	}

	public void addGrid(Grid grid) {
		for (int column = 0; column < gridCols; column++) {
			for (int row = 0; row < gridRows; row++) {
				this.data[position + column][row] = grid.getColumnRow(column,
						row);
			}
		}
		moveRight();
	}

	public void deleteLastGrid() {
		for (int column = 0; column < gridCols; column++) {
			for (int row = 0; row < gridRows; row++) {
				this.data[position + column][row] = false;
			}
		}
		moveLeft();
	}

	public void setDataFromBytes(byte[] aniBytes) {
		for (int column = 0; column < aniBytes.length; column++) {
			byte aniByte = aniBytes[column];
			this.data[column] = convertAnimationByteTo7Booleans(aniByte);
		}
		moveRight(); // set position to 5
	}


	@Override
	public AnimationType getAnimationType() {
		return AnimationType.GRAPHIC;
	}

	public byte[] getColumnsAsBytes() {
		byte[] allByteColumns = new byte[MAX_COLUMNS];
		// must avoid trailing $00
		int numberOfUsedColumns = 0;
		for (int colIndex = 0; colIndex < MAX_COLUMNS; colIndex++) {
			boolean[] bools = this.data[colIndex];
			assert (bools.length == 7);
			byte value = (byte) booleanArrayAsInt(bools);
			allByteColumns[colIndex] = value;
			if (value != 0) {
			  numberOfUsedColumns = colIndex+1;	
			}
		}
		byte[] usedByteColumns = new byte[numberOfUsedColumns];
		System.arraycopy(allByteColumns, 0, usedByteColumns, 0,
				numberOfUsedColumns);
		return usedByteColumns;
	}
	
	public static int booleanArrayAsInt(boolean[] array) {
		int value = 0;
		for (boolean bool : array) {
			value <<= 1; // shift left
			if (bool)
				value += 1; // set next bit
		}
		return value;
	}

	@Override
	public String toString() {
		return getAnimationType().getDescription();
	}

	public void setColumnRow(int column, int row, boolean value) {
		data[column][row] = value;
	}

	public void toggleColumnRow(int column, int row) {
		data[column][row] = !data[column][row];
	}

}
