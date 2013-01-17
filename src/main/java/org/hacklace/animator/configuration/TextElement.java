package org.hacklace.animator.configuration;

import org.hacklace.animator.ErrorContainer;

public abstract class TextElement {
	
	public abstract int[] getAnimationBytes();
	
	public abstract String getRawString();
	
	public abstract boolean isValid(ErrorContainer errorContainer);
	
	public abstract int getNumColumns();

	public final int getNumBytes() {
		return 1;
	}

}
