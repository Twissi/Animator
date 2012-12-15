package org.hacklace.animator.gui;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.TextDisplayBuffer;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;

public class EditAnimationPanel extends JPanel implements OptionsObserver, LedObserver {
	private static final long serialVersionUID = -5137928768652375360L;
	private AnimationOptionsPanel optionsPanel;
	private JPanel editTextPanel;
	private JTextField editTextField;
	private LedPanel prevLedPanel; // display of the previous frame
	private LedPanel ledPanel; // display/edit of the current frame
	private LedPanel nextLedPanel; // display of the next frame
	private JLabel prevLabel;
	private JLabel currentLabel;
	private JLabel nextLabel;
	private DisplayBuffer bufferRef; // our internal temporary displayBuffer for editing
	private DisplayBuffer origBuffer; // keep a reference to the original buffer for overwriting on save
	private int currentPosition = 0;

	public EditAnimationPanel() {
		optionsPanel = new AnimationOptionsPanel();
		add(optionsPanel);
		add(createLedPanelPanel());
		add(createEditTextPanel());
		reset();
		optionsPanel.addObserver(this);
	}
	
	/**
	 * Generate the panel of LedPanels for the edit view
	 * @return
	 */
	private JPanel createLedPanelPanel() {
		GridLayout gridLayout = new GridLayout(2, 3);
		gridLayout.setHgap(5);
		JPanel ledPanelPanel = new JPanel();
		ledPanelPanel.setLayout(gridLayout);
		// first row: 3 LedPanels
		prevLedPanel = new LedPanel(AnimatorGui.ROWS, AnimatorGui.COLUMNS);
		prevLedPanel.setEnabled(false);
		ledPanelPanel.add(prevLedPanel);
		ledPanel = new LedPanel(AnimatorGui.ROWS, AnimatorGui.COLUMNS);
		ledPanel.addObserver(this);
		ledPanelPanel.add(ledPanel);
		nextLedPanel = new LedPanel(AnimatorGui.ROWS, AnimatorGui.COLUMNS);
		nextLedPanel.setEnabled(false);
		ledPanelPanel.add(nextLedPanel);
		// second row: 3 labels for frame number
		prevLabel = new JLabel("-", null, JLabel.CENTER);
		ledPanelPanel.add(prevLabel);
		currentLabel = new JLabel("-", null, JLabel.CENTER);
		ledPanelPanel.add(currentLabel);
		nextLabel = new JLabel("-", null, JLabel.CENTER);
		ledPanelPanel.add(nextLabel);
		return ledPanelPanel;
	}
	
	private JPanel createEditTextPanel() {
		editTextPanel = new JPanel();
		editTextField = new JTextField(DisplayBuffer.getNumGrids());
		editTextField.addKeyListener(new KeyListener() {
			private void updateText() {
				((TextDisplayBuffer)bufferRef).setText(editTextField.getText());
				setFromDisplayBuffer(bufferRef, false);
			}
			@Override
			public void keyPressed(KeyEvent arg0) {
				updateText();
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				updateText();
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				updateText();
			}
		});
		editTextPanel.add(editTextField);
		return editTextPanel;
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
				panel.setLed(y, x, bufferRef.getValueAt(x + DisplayBuffer.COLUMNS * position, y));
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
			prevLabel.setText(Integer.toString(currentPosition - 1));
		} else {
			prevLedPanel.clear();
			prevLabel.setText("-");
		}
		copyBufferToPanel(currentPosition, ledPanel);
		currentLabel.setText(Integer.toString(currentPosition));
		if (currentPosition < DisplayBuffer.getNumGrids() - 1) {
			copyBufferToPanel(currentPosition + 1, nextLedPanel);
			nextLabel.setText(Integer.toString(currentPosition + 1));
		} else {
			nextLedPanel.clear();
			nextLabel.setText("-");
		}
		// set speed and delay
		optionsPanel.setOptions(buffer.getSpeed().getValue(), buffer.getDelay().getValue(), buffer.getDirection().getValue());
		// treat text buffers different from graphics buffers
		if (buffer.getAnimationType() == AnimationType.TEXT) {
			ledPanel.setEnabled(false);
			editTextPanel.setVisible(true);
			editTextField.setText(((TextDisplayBuffer)buffer).getText());
		} else {
			ledPanel.setEnabled(true);
			editTextPanel.setVisible(false);
		}
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
		// refresh list on home page because it contains the text for TextDisplayBuffers
		AnimatorGui.getInstance().getHomePanel().updateList(cm.getList(), true);
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
		// ignore the event if we don't have a valid buffer yet
		if (bufferRef == null) return;
		currentPosition = newPosition;
		setFromDisplayBuffer(bufferRef, false);
	}
	
	public void onDirectionChanged(Direction newDirection) {
		bufferRef.setDirection(newDirection);
	}
	
	public void onSaveAnimation() {
		saveBuffer();
		AnimatorGui.getInstance().setCurrentTabIndex(0);
	}
	
	public void onLedChange(int row, int column, boolean newValue) {
		// System.out.println("LED Changed: " + row + "/" + column + " to " + newValue);
		bufferRef.setValueAt(column + DisplayBuffer.COLUMNS * currentPosition, row, newValue);
	}
}
