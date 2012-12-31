package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import org.hacklace.animator.ModusByte;
import org.hacklace.animator.configuration.DirectMode;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.gui.EditPanel;

public class RawInputDirectModeApplyActionListener extends AbstractRawInputApplyActionListener {

	private JTextField rawInputRestOfLineTextField;

	public RawInputDirectModeApplyActionListener(JTextField rawInputRestOfLineTextField,
			EditPanel editPanel) {
		this.rawInputRestOfLineTextField = rawInputRestOfLineTextField;
		this.editPanel = editPanel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ModusByte modusByte = editPanel.getDisplayBuffer().getModusByte();
		DirectMode directMode = new DirectMode(rawInputRestOfLineTextField.getText());
		FullConfigLine fullLine = new FullConfigLine(modusByte, directMode);
		bufferFromString(fullLine);
	}
}