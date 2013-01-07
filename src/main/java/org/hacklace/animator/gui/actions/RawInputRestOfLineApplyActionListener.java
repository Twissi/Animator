package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.ModusByte;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.configuration.RestOfConfigLine;
import org.hacklace.animator.gui.EditPanel;

public class RawInputRestOfLineApplyActionListener extends AbstractRawInputApplyActionListener {

	private JTextField rawInputRestOfLineTextField;

	public RawInputRestOfLineApplyActionListener(JTextField rawInputRestOfLineTextField,
			EditPanel editPanel) {
		this.rawInputRestOfLineTextField = rawInputRestOfLineTextField;
		this.editPanel = editPanel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ErrorContainer errorContainer = new ErrorContainer();
		ModusByte modusByte = editPanel.getDisplayBuffer().getModusByte();
		RestOfConfigLine restOfLine = new RestOfConfigLine(rawInputRestOfLineTextField.getText(), errorContainer);
		FullConfigLine fullLine = new FullConfigLine(modusByte, restOfLine);
		bufferFromString(fullLine, errorContainer);
		if (errorContainer.containsErrorsOrWarnings()) {
			// TODO display error
		}
	}
	

}