package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;
import org.hacklace.animator.gui.EditGraphicPanel;

public abstract class AbstractFrameActionListener implements ActionListener {

	protected EditGraphicPanel editGraphicPanel;

	public AbstractFrameActionListener(EditGraphicPanel editGraphicPanel) {
		this.editGraphicPanel = editGraphicPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		genericAction();
	}

	public void genericAction() {
		GraphicDisplayBuffer buffer = editGraphicPanel.getBuffer();
		int currentFramePos = editGraphicPanel.getCurrentPosition();
		specificAction(buffer, currentFramePos);
		editGraphicPanel.updateUiFromDisplayBuffer();
	}

	protected abstract void specificAction(GraphicDisplayBuffer buffer,
			int currentFramePos);

}
