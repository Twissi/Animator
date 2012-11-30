package org.hacklace.animator.gui;

import javax.swing.JPanel;

import org.hacklace.animator.displaybuffer.DisplayBuffer;

public class EditAnimationPanel extends JPanel {
	private static final long serialVersionUID = -5137928768652375360L;
	private AnimationOptionsPanel optionsPanel;
	private LedPanel ledPanel;

	public EditAnimationPanel() {
		optionsPanel = new AnimationOptionsPanel();
		add(optionsPanel);
		ledPanel = new LedPanel(AnimatorGUI.ROWS, AnimatorGUI.COLUMNS);
		add(ledPanel);
		reset();
	}
	
	public void reset() {
		optionsPanel.reset();
	}
	
	public void setFromDisplayBuffer(DisplayBuffer buffer) {
		for (int x=0; x<DisplayBuffer.COLUMNS; x++) {
			for (int y=0; y<DisplayBuffer.ROWS; y++) {
				ledPanel.setLed(y, x, buffer.getValueAt(x, y));
			}
		}
	}
}
