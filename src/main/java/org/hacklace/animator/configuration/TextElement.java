package org.hacklace.animator.configuration;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.displaybuffer.Size;

public abstract class TextElement implements Size {
	
	public abstract int[] getAnimationBytes();
	
	public abstract String getRawString();
	
	public abstract boolean isValid(ErrorContainer errorContainer);
	
	@Override
	public abstract int getNumColumns();

	@Override
	public final int getNumBytes() {
		return 1;
	}

}
