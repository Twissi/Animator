package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.hacklace.animator.displaybuffer.ReferenceDisplayBuffer;
import org.hacklace.animator.enums.PredefinedAnimation;
import org.hacklace.animator.gui.AnimatorGui;
import org.hacklace.animator.gui.EditReferencePanel;

public class ChooseReferenceActionListener implements ActionListener {

	private EditReferencePanel editPanel;
	private AnimatorGui animatorGui;

	public ChooseReferenceActionListener(EditReferencePanel editPanel, AnimatorGui animatorGui) {
		this.editPanel = editPanel;
		this.animatorGui = animatorGui;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		PredefinedAnimation answer = AskForReferenceHelper.askForReference(animatorGui);
		if (answer == null) // clicked cancel
			return;
		ReferenceDisplayBuffer buffer = editPanel.getBuffer();
		buffer.setPredefinedAnimation(answer);
		editPanel.updateUiFromDisplayBuffer();
	}

}
