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
	}

}
