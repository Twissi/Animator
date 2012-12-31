package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import org.hacklace.animator.ModusByte;
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
		ModusByte modusByte = editPanel.getDisplayBuffer().getModusByte();
		String rawString = modusByte.getRawString() + rawInputRestOfLineTextField.getText();
		bufferFromString(rawString);
	}
	

}