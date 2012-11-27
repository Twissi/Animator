package org.hacklace.animator.displaybuffer;

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

	public void setDataFromBytes(byte[] aniBytes) {
		for (int column = 0; column < aniBytes.length; column++) {
			byte mask = 32; // 0b0100000; -- manuel; sorry, not working in my JDK7 :(
			for (int i = 0; i <= 6; i++) {
				byte maskResult = (byte) (mask & aniBytes[column]);
				this.data[column][i] = ((maskResult != 0) ? true : false);
				mask >>= 1;
			}
		}
		moveRight();

	}

}
