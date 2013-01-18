package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.hacklace.animator.gui.AnimatorGui;

public class CancelEditAction extends AbstractAction {
	private static final long serialVersionUID = 8730578405697706858L;
	private AnimatorGui animatorGui;

	public CancelEditAction(AnimatorGui animatorGui) {
		super("Cancel");
		this.animatorGui = animatorGui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		animatorGui.endEditMode();
	}
}