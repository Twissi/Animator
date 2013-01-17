package org.hacklace.animator.gui;

import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.MixedDisplayBuffer;

public class EditMixedPanel extends EditTextPanel {

	private static final long serialVersionUID = 4181833851656176456L;

	public EditMixedPanel(DisplayBuffer displayBuffer) {
		super(displayBuffer);
		ledPanel.setEnabled(false);
	}

	@Override
	protected int getMaximumGrid() {
		return getBuffer().getNumColumns() / GRID_COLS + 1;
	}
	
	@Override
	public MixedDisplayBuffer getBuffer() {
	  return (MixedDisplayBuffer) super.getBuffer();	
	}

}
