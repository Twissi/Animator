package org.hacklace.animator.gui.actions;

import javax.swing.JOptionPane;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.enums.PredefinedAnimation;
import org.hacklace.animator.gui.AnimatorGui;

public class AskForReferenceHelper {
	
	public static PredefinedAnimation askForReference(AnimatorGui animatorGui) {

		PredefinedAnimation result = (PredefinedAnimation) JOptionPane
				.showInputDialog(
						animatorGui,
						"Please select the number of the referenced animation. A is the first, B the second, etc.",
						"Animation number", JOptionPane.QUESTION_MESSAGE, null,
						PredefinedAnimation.getList(), PredefinedAnimation
								.getPredefinedAnimationByIndex('A',
										new ErrorContainer()));
		return result;
	}
}
