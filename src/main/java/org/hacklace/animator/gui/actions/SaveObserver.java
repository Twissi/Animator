package org.hacklace.animator.gui.actions;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.gui.BufferReferencer;

public interface SaveObserver extends BufferReferencer {

	@Override
	public DisplayBuffer getBuffer();

	public void saveBuffer();

	@Override
	public void showErrors(ErrorContainer errorContainer);

}
