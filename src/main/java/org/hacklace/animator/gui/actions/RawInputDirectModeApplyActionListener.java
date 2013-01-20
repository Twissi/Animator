/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.ModusByte;
import org.hacklace.animator.configuration.DirectMode;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.gui.AnimatorGui;
import org.hacklace.animator.gui.EditPanel;

public class RawInputDirectModeApplyActionListener extends AbstractRawInputApplyActionListener {

	private JTextField rawInputRestOfLineTextField;

	public RawInputDirectModeApplyActionListener(JTextField rawInputRestOfLineTextField,
			EditPanel editPanel, AnimatorGui animatorGui) {
		super(editPanel, animatorGui);
		this.rawInputRestOfLineTextField = rawInputRestOfLineTextField;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		ModusByte modusByte = editPanel.getDisplayBuffer().getModusByte();
		DirectMode directMode = new DirectMode(rawInputRestOfLineTextField.getText());
		FullConfigLine fullLine = new FullConfigLine(modusByte, directMode);
		ErrorContainer errorContainer = new ErrorContainer();
		bufferFromString(fullLine, errorContainer);
		if (errorContainer.containsFailure()) {
			editPanel.showErrors(errorContainer);
		}
	}
}
