package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.gui.AnimatorGui;

public class SaveAnimationAction extends AbstractAction {

	private static final long serialVersionUID = -5813301123661228603L;
	private SaveObserver saveObserver;
	private AnimatorGui animatorGui;

	public SaveAnimationAction(
			SaveObserver saveObserver, AnimatorGui animatorGui) {
		super("Save");

		this.saveObserver = saveObserver;
		this.animatorGui = animatorGui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ErrorContainer errorContainer = new ErrorContainer();

		boolean isSaveAble = saveObserver.getBuffer().isSaveable(errorContainer);

		if (isSaveAble) {
			saveObserver.saveBuffer();
			animatorGui.endEditMode();
		} else {
			saveObserver.showErrors(errorContainer);
			JOptionPane.showMessageDialog(null,
					"The buffer cannot be saved.", "Error saving",
					JOptionPane.ERROR_MESSAGE);

		}
	}
}