package org.hacklace.animator.gui;

import javax.swing.JPanel;

public class EditAnimationPanel extends JPanel {
	private static final long serialVersionUID = -5137928768652375360L;

	public EditAnimationPanel() {
		add(new AnimationOptionsPanel());
		add(new GridPanel(AnimatorGUI.ROWS, AnimatorGUI.COLUMNS));
	}	
}
