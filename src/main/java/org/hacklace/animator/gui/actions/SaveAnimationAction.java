package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.gui.AnimatorGui;
import org.hacklace.animator.gui.EditPanel;

public class SaveAnimationAction extends AbstractAction {

	private static final long serialVersionUID = -5813301123661228603L;
	private EditPanel editPanel;

	public SaveAnimationAction(
			EditPanel editPanel) {
		super("Save");

		this.editPanel = editPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ErrorContainer errorContainer = new ErrorContainer();

		boolean isSaveAble = editPanel.buffer.isSaveable(errorContainer);

		if (isSaveAble) {
			editPanel.saveBuffer();
			AnimatorGui.getInstance().endEditMode();
		} else {
			editPanel.showErrors(errorContainer);
			JOptionPane.showMessageDialog(null,
					"The buffer can not be saved.", "Error saving",
					JOptionPane.ERROR_MESSAGE);

		}
	}
}