package org.hacklace.animator.gui.actions;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.displaybuffer.DisplayBuffer;

public interface SaveObserver {

	public DisplayBuffer getBuffer();

	public void saveBuffer();

	public void showErrors(ErrorContainer errorContainer);

}
