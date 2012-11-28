package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.enums.AnimationType;

public class GraphicDisplayBuffer extends DisplayBuffer {
	
	private int usedBytes; // must avoid $00 in the end!

	public GraphicDisplayBuffer() {
		super();
		this.usedBytes = 0;
	}

	@Override
	public int getStepWidth() {
		return 5;
	}

	public void addGrid(Grid grid) {
        usedBytes+=COLUMNS;
		boolean[][] gridData = grid.getData();
		for (int column = 0; column < 5; column++) {
			for (int row = 0; row < 7; row++) {
				this.data[position + column][row] = gridData[column][row];
			}
		}
		moveRight();
	}
	
	public void deleteLastGrid() {
		usedBytes-=COLUMNS;;
		moveLeft();
	}

	public void setDataFromBytes(byte[] aniBytes) {
		for (int column = 0; column < aniBytes.length; column++) {
			byte mask = 32; // 0b0100000; -- manuel; sorry, not working in my JDK7 :(
			byte aniByte = aniBytes[column];
			if (aniByte != 0) {
				this.usedBytes = column; // highest non-zero column
			}
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
		byte[] columns = new byte[this.usedBytes];
		for (int c = 0; c < this.usedBytes; c++) {
			boolean[] bools = this.data[c];
			assert (bools.length == 7);
			int value = 0;
			for (boolean bool : bools) {
				value <<= 1; // shift left
				if (bool) value += 1; // set next bit 
			}
			columns[c] = (byte) value;
		}
		return columns;
	}
	
	

}
