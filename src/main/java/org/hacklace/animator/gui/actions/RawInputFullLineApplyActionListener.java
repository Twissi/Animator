package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.gui.EditPanel;

public class RawInputFullLineApplyActionListener extends
		AbstractRawInputApplyActionListener {

	private JTextField rawInputFullLineTextField;

	public RawInputFullLineApplyActionListener(
			JTextField rawInputFullLineTextField, EditPanel editPanel) {
		this.rawInputFullLineTextField = rawInputFullLineTextField;
		this.editPanel = editPanel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String rawString = rawInputFullLineTextField.getText();
		FullConfigLine fullLine = new FullConfigLine(rawString);
		ErrorContainer errorContainer = new ErrorContainer();
		bufferFromString(fullLine, errorContainer);
		if (errorContainer.containsFailure()) {
			// TODO display error
		}
	}
}