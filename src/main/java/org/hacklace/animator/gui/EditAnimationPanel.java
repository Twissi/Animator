package org.hacklace.animator.gui;

import javax.swing.JPanel;

import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Speed;

public class EditAnimationPanel extends JPanel implements OptionsObserver {
	private static final long serialVersionUID = -5137928768652375360L;
	private AnimationOptionsPanel optionsPanel;
	private LedPanel ledPanel;
	private DisplayBuffer bufferRef;

	public EditAnimationPanel() {
		optionsPanel = new AnimationOptionsPanel();
		add(optionsPanel);
		ledPanel = new LedPanel(AnimatorGui.ROWS, AnimatorGui.COLUMNS);
		add(ledPanel);
		reset();
		optionsPanel.addObserver(this);
	}
	
	public void reset() {
		// TODO optionsPanel.reset();
	}
	
	public void setFromDisplayBuffer(DisplayBuffer buffer) {
		bufferRef = buffer;
		for (int x=0; x<DisplayBuffer.COLUMNS; x++) {
			for (int y=0; y<DisplayBuffer.ROWS; y++) {
				ledPanel.setLed(y, x, buffer.getValueAt(x, y));
			}
		}
		optionsPanel.setOptions(buffer.getSpeed().getValue(), buffer.getDelay().getValue());
	}
	
	public void onSpeedChanged(Speed newSpeed) {
		bufferRef.setSpeed(newSpeed);
	}
	
	public void onDelayChanged(Delay newDelay) {
		bufferRef.setDelay(newDelay);
	}
	
}
