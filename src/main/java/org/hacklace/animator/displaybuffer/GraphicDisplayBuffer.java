package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.enums.AnimationType;

public class GraphicDisplayBuffer extends DisplayBuffer {

	public GraphicDisplayBuffer() {
		super();
	}

	public void addGrid(Grid grid) {
		for (int column = 0; column < COLUMNS; column++) {
			for (int row = 0; row < ROWS; row++) {
				this.data[position + column][row] = grid.getColumnRow(column,
						row);
			}
		}
		moveRight();
	}

	public void deleteLastGrid() {
		for (int column = 0; column < COLUMNS; column++) {
			for (int row = 0; row < ROWS; row++) {
				this.data[position + column][row] = false;
			}
		}
		moveLeft();
	}

	public void setDataFromBytes(byte[] aniBytes) {
		for (int column = 0; column < aniBytes.length; column++) {
			byte aniByte = aniBytes[column];
			this.data[column] = booleanArray7FromByte(aniByte);
		}
		moveRight(); // set position to 5
	}

	/**
	 * The first bit (bit 7) is assumed to be 0 (should be the case for
	 * animation bytes)
	 * 
	 * @param bits
	 * @return array with 7 booleans
	 */
	public static boolean[] booleanArray7FromByte(byte aniByte) {
		byte mask = 64; // 0100 0000
		boolean[] returnArray = new boolean[7];
		for (int row = 0; row <= 6; row++) {
			byte maskResult = (byte) (mask & aniByte);
			returnArray[row] = ((maskResult != 0) ? true : false);
			mask >>= 1;
		}
		return returnArray;
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
		return "Grafische Animation";
	}

	public void setColumnRow(int column, int row, boolean value) {
		data[column][row] = value;
	}

	public void toggleColumnRow(int column, int row) {
		data[column][row] = !data[column][row];
	}

}
