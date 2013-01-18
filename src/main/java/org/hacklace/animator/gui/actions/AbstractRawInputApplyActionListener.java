package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.gui.AnimatorGui;
import org.hacklace.animator.gui.EditPanel;

public abstract class AbstractRawInputApplyActionListener implements
		ActionListener {

	protected EditPanel editPanel;
	protected AnimatorGui animatorGui;
	
	protected AbstractRawInputApplyActionListener(EditPanel editPanel, AnimatorGui animatorGui) {
		this.editPanel = editPanel;
		this.animatorGui = animatorGui;
	}

	@Override
	abstract public void actionPerformed(ActionEvent event);

	protected void bufferFromString(FullConfigLine fullLine,
			ErrorContainer errorContainer) {
		try {
			DisplayBuffer newBuffer = DisplayBuffer.createBufferFromLine(fullLine,
					errorContainer);
			if (newBuffer == null) {
				JOptionPane.showMessageDialog(null,
						DisplayBuffer.ZERO_MODUS_BYTE, "Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (newBuffer.getAnimationType() == editPanel
					.getDisplayBuffer().getAnimationType()) {
				// same type of animation, just switch buffers
				editPanel.setNewDisplayBuffer(newBuffer);
				editPanel.onRawTextChanged();
			} else {
				// different type of animation, switch editors
				animatorGui.endEditMode();
				animatorGui.startEditMode(newBuffer);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Invalid raw string supplied. "
					+ ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

}
