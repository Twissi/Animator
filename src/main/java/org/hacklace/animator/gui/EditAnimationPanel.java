package org.hacklace.animator.gui;

import javax.swing.JPanel;

import org.hacklace.animator.displaybuffer.DisplayBuffer;

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
	
	public void setFromDisplayBuffer(DisplayBuffer buffer) {
		for (int x=0; x<buffer.COLUMNS; x++) {
			for (int y=0; y<DisplayBuffer.ROWS; y++) {
				gridPanel.setPixel(y, x, buffer.getValueAt(x, y));
			}
		}
	}
}
