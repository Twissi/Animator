package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;

public abstract class DisplayBuffer {

	protected boolean[][] data;
	
	protected static final int ROWS = 7;
	protected static final  int WIDTH = 200;
	
	protected int position;
	protected Direction direction;
	protected Speed speed;
	protected Delay delay;
	
	public DisplayBuffer() {
		data = new boolean[WIDTH][ROWS];
		position = 0;
		direction = Direction.FORWARD;
		speed = Speed.ZERO;
		delay = Delay.ZERO;
	}
	
	public abstract Grid getPrevious();	
	public abstract Grid getCurrent();
	public abstract Grid getNext();
	
	public void rewind() {
		position = 0;
	}
}
