package org.hacklace.animator.gui;

import org.hacklace.animator.IniConf;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.ReferenceDisplayBuffer;

public class EditReferencePanel extends EditPanel {

	private static final long serialVersionUID = 1648909855223726429L;

	public EditReferencePanel(DisplayBuffer displayBuffer) {
		super(displayBuffer);
		ledPanel.setEnabled(false);
	}

	@Override
	protected int getMaximumGrid() {
		int columns = ((ReferenceDisplayBuffer) buffer).countUsedColumns();
		int grids = ((int) (columns / GRID_COLS)) - NUM_GRIDS_TO_SHOW;
		if (grids < 0)
			grids = 0;
		return grids;
	}

}
