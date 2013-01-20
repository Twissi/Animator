/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
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
	public void actionPerformed(ActionEvent event) {
		PredefinedAnimation answer = AskForReferenceHelper.askForReference(animatorGui);
		if (answer == null) // clicked cancel
			return;
		ReferenceDisplayBuffer buffer = editPanel.getBuffer();
		buffer.setPredefinedAnimation(answer);
		editPanel.updateUiFromDisplayBuffer();
	}

}
