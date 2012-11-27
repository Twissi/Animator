package org.hacklace.animator.gui;

import javax.swing.JPanel;

public class EditAnimationPanel extends JPanel {
	private static final long serialVersionUID = -5137928768652375360L;
	private AnimationOptionsPanel optionsPanel;
	private GridPanel gridPanel;

	public EditAnimationPanel() {
		optionsPanel = new AnimationOptionsPanel();
		add(optionsPanel);
		gridPanel = new GridPanel(AnimatorGUI.ROWS, AnimatorGUI.COLUMNS);
		add(gridPanel);
		reset();
	}
	
	public void reset() {
		optionsPanel.reset();
	}
}
