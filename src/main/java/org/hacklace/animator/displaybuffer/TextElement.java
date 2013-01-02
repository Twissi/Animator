package org.hacklace.animator.displaybuffer;

public abstract class TextElement implements Size {
	
	public abstract int[] getAnimationBytes();
	
	public abstract String getRawString();
	
	public abstract boolean isValid();
	
	@Override
	public abstract int getNumColumns();

	@Override
	public int getNumBytes() {
		return getNumColumns();
	}

}
