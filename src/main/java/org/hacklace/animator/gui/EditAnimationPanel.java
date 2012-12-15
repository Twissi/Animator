package org.hacklace.animator.gui;

import java.util.List;

import javax.swing.JPanel;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Speed;

public class EditAnimationPanel extends JPanel implements OptionsObserver {
	private static final long serialVersionUID = -5137928768652375360L;
	private AnimationOptionsPanel optionsPanel;
	private LedPanel prevLedPanel; // display of the previous frame
	private LedPanel ledPanel; // display/edit of the current frame
	private LedPanel nextLedPanel; // display of the next frame
	private DisplayBuffer bufferRef; // our internal temporary displayBuffer for editing
	private DisplayBuffer origBuffer; // keep a reference to the original buffer for overwriting on save
	private int currentPosition = 0;

	public EditAnimationPanel() {
		optionsPanel = new AnimationOptionsPanel();
		add(optionsPanel);
		add(getLedPanelPanel());
		reset();
		optionsPanel.addObserver(this);
	}
	
	/**
	 * Generate the panel of LedPanels for the edit view
	 * @return
	 */
	private JPanel getLedPanelPanel() {
		JPanel ledPanelPanel = new JPanel();
		prevLedPanel = new LedPanel(AnimatorGui.ROWS, AnimatorGui.COLUMNS);
		prevLedPanel.setEnabled(false);
		ledPanelPanel.add(prevLedPanel);
		ledPanel = new LedPanel(AnimatorGui.ROWS, AnimatorGui.COLUMNS);
		ledPanelPanel.add(ledPanel);
		nextLedPanel = new LedPanel(AnimatorGui.ROWS, AnimatorGui.COLUMNS);
		nextLedPanel.setEnabled(false);
		ledPanelPanel.add(nextLedPanel);
		return ledPanelPanel;
	}
	
	/* see note below...
	public void copyGridDataToPanel(Grid grid, LedPanel panel) {
		for (int x=0; x<DisplayBuffer.COLUMNS; x++) {
			for (int y=0; y<DisplayBuffer.ROWS; y++) {
				panel.setLed(y, x, grid.getData()[x][y]);
			}
		}
	}
	*/
	
	public void copyBufferToPanel(int position, LedPanel panel) {
		for (int x=0; x<DisplayBuffer.COLUMNS; x++) {
			for (int y=0; y<DisplayBuffer.ROWS; y++) {
				panel.setLed(y, x, bufferRef.getValueAt(x + bufferRef.getStepWidth() * position, y));
			}
		}
	}
	
	public void setFromDisplayBuffer(DisplayBuffer buffer, boolean clone) {
		if (clone) {
			// clone the display buffer so we can edit and cancel without changing the original data
			bufferRef = buffer.clone();
			origBuffer = buffer;
		}
		/* The buffer implementation is not working or at least very much unintuitive.
		 * I'll implement direct access to the data until I've talked to the team.
		 * TODO: Fix this!
		// get the previous, current and next grid for displaying. Use null if we're at the start or end of an animation
		Grid prevGrid = (buffer.getPosition() > 0) ? buffer.getGrid(-1) : null;
		Grid grid = buffer.getCurrent();
		Grid nextGrid = (buffer.getPosition() < buffer.getNumGrids() - 1) ? buffer.getNext() : null;
		if (prevGrid != null) {
			copyGridDataToPanel(prevGrid, prevLedPanel);
		} else {
			prevLedPanel.clear();
		}
		copyGridDataToPanel(grid, ledPanel);
		if (nextGrid != null) {
			copyGridDataToPanel(nextGrid, nextLedPanel);
		} else {
			nextLedPanel.clear();
		}
		 */
		if (currentPosition > 0) {
			copyBufferToPanel(currentPosition - 1, prevLedPanel);
			prevLedPanel.setEnabled(true);
		} else {
			prevLedPanel.clear();
			prevLedPanel.setEnabled(false);
		}
		copyBufferToPanel(currentPosition, ledPanel);
		if (currentPosition < bufferRef.getNumGrids() - 1) {
			copyBufferToPanel(currentPosition + 1, nextLedPanel);
			nextLedPanel.setEnabled(true);
		} else {
			nextLedPanel.clear();
			nextLedPanel.setEnabled(false);
		}
		// set speed and delay
		optionsPanel.setOptions(buffer.getSpeed().getValue(), buffer.getDelay().getValue());
	}
	
	public void reset() {
		currentPosition = 0;
		optionsPanel.setPosition(currentPosition);
	}
	
	/**
	 * Switch our temporary DisplayBuffer to the original passed on startEdit
	 * The buffer must not be touched anymore after this because we switch them(!)
	 */
	public void saveBuffer() {
		HacklaceConfigManager cm = AnimatorGui.getInstance().getHacklaceConfigManager();
		List <DisplayBuffer> list = cm.getList();
		list.set(list.indexOf(origBuffer), bufferRef);
		bufferRef = null;
		origBuffer = null;
	}
	
	public void setMaxPosition(int maxPosition) {
		optionsPanel.setMaxPosition(maxPosition);
	}
	
	public void onSpeedChanged(Speed newSpeed) {
		bufferRef.setSpeed(newSpeed);
	}
	
	public void onDelayChanged(Delay newDelay) {
		bufferRef.setDelay(newDelay);
	}
	
	public void onPositionChanged(int newPosition) {
		// TODO ... bufferRef.setPosition...
		currentPosition = newPosition;
		setFromDisplayBuffer(bufferRef, false);
	}
	
	public void onSaveAnimation() {
		saveBuffer();
		AnimatorGui.getInstance().setCurrentTabIndex(0);
	}
	
}
