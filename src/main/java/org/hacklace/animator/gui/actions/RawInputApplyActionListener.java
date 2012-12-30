package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.gui.EditPanel;

public class RawInputApplyActionListener implements ActionListener {

	private JTextField rawInputTextField;
	private EditPanel editPanel;

	public RawInputApplyActionListener(JTextField rawInputTextField,
			EditPanel editPanel) {
		this.rawInputTextField = rawInputTextField;
		this.editPanel = editPanel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String rawString = rawInputTextField.getText();
		try {
			DisplayBuffer buffer = DisplayBuffer
					.createBufferFromLine(rawString);
			// it worked without error, we can now switch buffers
			editPanel.setFromDisplayBuffer(buffer);
			editPanel.onRawTextChanged();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"Invalid raw string supplied. " + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}

	}
}