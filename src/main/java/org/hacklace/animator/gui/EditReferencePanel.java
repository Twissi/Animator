package org.hacklace.animator.gui;

import org.hacklace.animator.displaybuffer.DisplayBuffer;

public class EditReferencePanel extends EditPanel {

	private static final long serialVersionUID = 1648909855223726429L;
	
	public EditReferencePanel(DisplayBuffer displayBuffer) {
		super(displayBuffer);
		ledPanel.setEnabled(false);
	}

}
