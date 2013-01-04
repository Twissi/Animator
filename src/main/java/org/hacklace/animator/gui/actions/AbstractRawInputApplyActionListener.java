package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.gui.EditPanel;

public abstract class AbstractRawInputApplyActionListener implements
		ActionListener {

	protected EditPanel editPanel;

	@Override
	abstract public void actionPerformed(ActionEvent arg0);

	protected void bufferFromString(FullConfigLine fullLine) {
		try {
			DisplayBuffer buffer = DisplayBuffer.createBufferFromLine(fullLine);
			// it worked without error, we can now apply the result
			if (buffer.getAnimationType() == editPanel.getDisplayBuffer()
					.getAnimationType()) {
				// same type of animation, just switch buffers
				editPanel.setFromDisplayBuffer(buffer);
				editPanel.onRawTextChanged();
			} else {
				// different type of animation, we don't allow this.
				JOptionPane
						.showMessageDialog(
								null,
								"The supplied raw string would change the animation type. Sorry, this is not allowed.",
								"Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Invalid raw string supplied. "
					+ ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

}
