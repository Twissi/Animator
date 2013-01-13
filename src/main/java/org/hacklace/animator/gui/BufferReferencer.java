package org.hacklace.animator.gui;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.displaybuffer.DisplayBuffer;

public interface BufferReferencer {
	public DisplayBuffer getBuffer();
	public void showErrors(ErrorContainer errorContainer);
}
