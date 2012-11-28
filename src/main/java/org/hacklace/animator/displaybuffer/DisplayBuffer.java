package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;

public abstract class DisplayBuffer {

	protected boolean[][] data;
	
	public static final int ROWS = 7;
	public static final int COLUMNS = 5;
	protected static final  int MAX_COLUMNS = 200;
	
	protected int position;
	protected Direction direction;
	protected Speed speed;
	protected Delay delay;
	
	public DisplayBuffer() {
		data = new boolean[MAX_COLUMNS][ROWS];
		position = 0;
		direction = Direction.FORWARD;
		speed = Speed.ZERO;
		delay = Delay.ZERO;
	}
	
	public boolean getValueAt(int x, int y) {
		return data[x][y];
	}

	public abstract int getStepWidth();
	
	/*
	 * Real code
	 */
	
	public void rewind() {
		position = 0;
	}
	
	public void moveLeft() {
		position = Math.max(0, position - getStepWidth());
	}
	
	public void moveRight() {
		position = Math.min( position + getStepWidth(), MAX_COLUMNS - 1);
	}
	
	/*
	 * Getter/Setter
	 */
	
	private Grid getGrid(int offset) {
		Grid g = new Grid(ROWS, COLUMNS);
		boolean[][] gridData = g.getData();
		
		for(int column = 0; column < COLUMNS; column++ ) {
			for(int row = 0; row < ROWS; row++ ) {
				gridData[column][row] = data[position + offset + column][row];
			}
		}
		
		return g;
	}
	
	public Grid getPrevious() {
		return getGrid(-2 * getStepWidth());
	}
	public Grid getCurrent() {
		return getGrid(-getStepWidth());
	}
	
	public Grid getNext() {
		return getGrid(0);
	}
	
	public int getPosition() {
		return position;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public Speed getSpeed() {
		return speed;
	}

	public Delay getDelay() {
		return delay;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setSpeed(Speed speed) {
		this.speed = speed;
	}

	public void setDelay(Delay delay) {
		this.delay = delay;
	}
	
    public abstract AnimationType getAnimationType();


}
