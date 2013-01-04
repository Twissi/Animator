package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.ErrorContainer;

public abstract class TextElement implements Size {
	
	public abstract int[] getAnimationBytes();
	
	public abstract String getRawString();
	
	public abstract boolean isValid(ErrorContainer errorContainer);
	
	@Override
	public abstract int getNumColumns();

	@Override
	public int getNumBytes() {
		return getNumColumns();
	}

}
