package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.ModusByte;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.configuration.RestOfConfigLine;
import org.hacklace.animator.gui.AnimatorGui;
import org.hacklace.animator.gui.EditPanel;

public class RawInputRestOfLineApplyActionListener extends AbstractRawInputApplyActionListener {

	private JTextField rawInputRestOfLineTextField;

	public RawInputRestOfLineApplyActionListener(JTextField rawInputRestOfLineTextField,
			EditPanel editPanel, AnimatorGui animatorGui) {
		super(editPanel, animatorGui);
		this.rawInputRestOfLineTextField = rawInputRestOfLineTextField;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		ErrorContainer errorContainer = new ErrorContainer();
		ModusByte modusByte = editPanel.getDisplayBuffer().getModusByte();
		RestOfConfigLine restOfLine = new RestOfConfigLine(rawInputRestOfLineTextField.getText(), errorContainer);
		FullConfigLine fullLine = new FullConfigLine(modusByte, restOfLine);
		bufferFromString(fullLine, errorContainer);
		if (errorContainer.containsErrorsOrWarnings()) {
			editPanel.showErrors(errorContainer);
		}
	}
	

}