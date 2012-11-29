package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.enums.AnimationType;

public class GraphicDisplayBuffer extends DisplayBuffer {

	public GraphicDisplayBuffer() {
		super();
	}

	@Override
	public int getStepWidth() {
		return 5;
	}

	public void addGrid(Grid grid) {
		boolean[][] gridData = grid.getData();
		for (int column = 0; column < 5; column++) {
			for (int row = 0; row < 7; row++) {
				this.data[position + column][row] = gridData[column][row];
			}
		}
		moveRight();
	}
	
	public void deleteLastGrid() {
		// TODO
		moveLeft();
	}

	public void setDataFromBytes(byte[] aniBytes) {
		for (int column = 0; column < aniBytes.length; column++) {
			byte mask = 64; // 0b1000000; -- manuel; sorry, not working in my JDK7 :(
			byte aniByte = aniBytes[column];
			for (int row = 0; row <= 6; row++) {
				byte maskResult = (byte) (mask & aniByte);
				this.data[column][row] = ((maskResult != 0) ? true : false);
				mask >>= 1;
			}
		}
		moveRight(); // set position to 5
		// make sure this.usedBytes is divisible by 5 (number of COLUMNS)
//		if (usedBytes % 5 != 0) {
//		  usedBytes = ((usedBytes / 5) + 1) * 5;
//		}
	}

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.GRAPHIC;
	}

	public byte[] getColumnsAsBytes() {
		byte[] allByteColumns = new byte[MAX_COLUMNS];
		for (int c = 0; c < MAX_COLUMNS; c++) {
			boolean[] bools = this.data[c];
			assert (bools.length == 7);
			int value = 0;
			for (boolean bool : bools) {
				value <<= 1; // shift left
				if (bool) value += 1; // set next bit 
			}
			allByteColumns[c] = (byte) value;
		}
		// must avoid trailing $00
		int numberOfUsedColumns = MAX_COLUMNS;
		while (allByteColumns[numberOfUsedColumns-1] == 0) {
			numberOfUsedColumns--;
		}
		byte[] usedByteColumns = new byte[numberOfUsedColumns];
		System.arraycopy(allByteColumns, 0, usedByteColumns, 0, numberOfUsedColumns);
		return usedByteColumns;
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
