package org.hacklace.animator.gui;

import org.hacklace.animator.displaybuffer.DisplayBuffer;

public class EditMixedPanel extends EditPanel {

	private static final long serialVersionUID = 4181833851656176456L;

	public EditMixedPanel(DisplayBuffer displayBuffer) {
		super(displayBuffer);
		ledPanel.setEnabled(false);
	}

	protected int getMaximumGrid() {
		return buffer.getNumColumns() / GRID_COLS + 1;
	}

}
