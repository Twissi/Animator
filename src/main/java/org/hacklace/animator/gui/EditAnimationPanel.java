package org.hacklace.animator.gui;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.*;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.FontUtil;
import org.hacklace.animator.displaybuffer.TextDisplayBuffer;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;

public class EditAnimationPanel extends JPanel implements OptionsObserver,
		LedObserver {
	private static final long serialVersionUID = -5137928768652375360L;

	private static final int VIRTUAL_KEYS_PER_ROW = 15;
	private AnimationOptionsPanel optionsPanel;
	private JPanel editTextPanel;
	private JPanel virtualKeyboardPanel;
	private JTextField editTextField;
	private JTextField rawInputTextField;
	private LedPanel prevLedPanel; // display of the previous frame
	private LedPanel ledPanel; // display/edit of the current frame
	private LedPanel nextLedPanel; // display of the next frame
	private JLabel prevLabel;
	private JLabel currentLabel;
	private JLabel nextLabel;
	private DisplayBuffer bufferRef; // our internal temporary displayBuffer for
										// editing
	private DisplayBuffer origBuffer; // keep a reference to the original buffer
										// for overwriting on save
	private int currentPosition = 0;

	public EditAnimationPanel() {
		optionsPanel = new AnimationOptionsPanel();
		add(optionsPanel);
		add(createLedPanelPanel());
		add(createEditTextPanel());
		add(createVirtualKeyboardPanel());
		add(createRawInputPanel());
		reset();
		optionsPanel.addObserver(this);
	}

	private JPanel createVirtualKeyboardPanel() {
		virtualKeyboardPanel = new JPanel();
		virtualKeyboardPanel.setLayout(new GridLayout(0, 1));
		ActionListener virtualKeyboardListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				VirtualKeyboardButton vk = (VirtualKeyboardButton) arg0
						.getSource();
				String chars = vk.getString();
				int pos = editTextField.getCaretPosition();
				try {
					editTextField.getDocument().insertString(pos, chars, null);
					((TextDisplayBuffer) bufferRef).setText(editTextField
							.getText());
					setFromDisplayBuffer(bufferRef, false);
				} catch (BadLocationException e) {
					// just do nothing, this should not happen anyways
				}
			}
		};
		int numButtons = FontUtil.HIGHEST_SPECIAL_INDEX
				- FontUtil.LOWEST_SPECIAL_INDEX + 1;
		VirtualKeyboardButton[] buttons = new VirtualKeyboardButton[numButtons];
		int numKeyboardRows = numButtons / VIRTUAL_KEYS_PER_ROW + 1;
		JPanel[] keyboardRows = new JPanel[numKeyboardRows];
		for (int i = 0; i < keyboardRows.length; i++) {
			keyboardRows[i] = new JPanel();
		}
		int currentRow = -1;
		for (int i = 0; i < numButtons; i++) {
			if (i % VIRTUAL_KEYS_PER_ROW == 0) {
				currentRow++;
			}
			buttons[i] = new VirtualKeyboardButton(i
					+ FontUtil.LOWEST_SPECIAL_INDEX);
			buttons[i].addActionListener(virtualKeyboardListener);
			keyboardRows[currentRow].add(buttons[i]);
		}
		for (int i = 0; i < keyboardRows.length; i++) {
			virtualKeyboardPanel.add(keyboardRows[i]);
		}
		return virtualKeyboardPanel;
	}

	/**
	 * Generate the panel of LedPanels for the edit view
	 * 
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
		prevLabel = new JLabel("-", null, SwingConstants.CENTER);
		ledPanelPanel.add(prevLabel);
		currentLabel = new JLabel("-", null, SwingConstants.CENTER);
		ledPanelPanel.add(currentLabel);
		nextLabel = new JLabel("-", null, SwingConstants.CENTER);
		ledPanelPanel.add(nextLabel);
		return ledPanelPanel;
	}

	private JPanel createEditTextPanel() {
		editTextPanel = new JPanel();
		editTextField = new JTextField(DisplayBuffer.getNumGrids());
		PlainDocument doc = new PlainDocument();
		doc.setDocumentFilter(new DocumentFilter() {
			public void insertString(FilterBypass fb, int offs, String str,
					AttributeSet a) throws BadLocationException {
				if ((fb.getDocument().getLength() + str.length()) <= DisplayBuffer.getNumGrids())
					super.insertString(fb, offs, str, a);
				else
					Toolkit.getDefaultToolkit().beep();
			}

			public void replace(FilterBypass fb, int offs, int length,
					String str, AttributeSet a) throws BadLocationException {
				if ((fb.getDocument().getLength() + str.length() - length) <= DisplayBuffer.getNumGrids())
					super.replace(fb, offs, length, str, a);
				else
					Toolkit.getDefaultToolkit().beep();
			}
		});
		editTextField.setDocument(doc);
		editTextField.addKeyListener(new KeyListener() {
			private void updateText() {
				((TextDisplayBuffer) bufferRef).setText(editTextField.getText());
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

	public JPanel createRawInputPanel() {
		JPanel rawInputPanel = new JPanel();
		JLabel label = new JLabel("Raw data:");
		rawInputPanel.add(label);
		rawInputTextField = new JTextField(DisplayBuffer.getNumGrids());
		rawInputTextField.setText("");
		rawInputPanel.add(rawInputTextField);
		JButton button = new JButton("Apply");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String rawString = rawInputTextField.getText().trim();
				try {
					DisplayBuffer tmp = DisplayBuffer.createBufferFromLine(
							rawString, 0);
					// it worked without error, we can now switch buffers
					bufferRef = tmp;
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(
							null,
							"Invalid raw string supplied. Message: "
									+ ex.toString(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				setFromDisplayBuffer(bufferRef, false);
			}
		});
		rawInputPanel.add(button);
		return rawInputPanel;
	}

	/*
	 * see note below... public void copyGridDataToPanel(Grid grid, LedPanel
	 * panel) { for (int x=0; x<DisplayBuffer.COLUMNS; x++) { for (int y=0;
	 * y<DisplayBuffer.ROWS; y++) { panel.setLed(y, x, grid.getData()[x][y]); }
	 * } }
	 */

	public void copyBufferToPanel(int position, LedPanel panel) {
		for (int x = 0; x < DisplayBuffer.COLUMNS; x++) {
			for (int y = 0; y < DisplayBuffer.ROWS; y++) {
				panel.setLed(
						y,
						x,
						bufferRef.getValueAt(x + DisplayBuffer.COLUMNS
								* position, y));
			}
		}
	}

	public void setFromDisplayBuffer(DisplayBuffer buffer, boolean clone) {
		if (clone) {
			// clone the display buffer so we can edit and cancel without
			// changing the original data
			bufferRef = buffer.clone();
			origBuffer = buffer;
		}
		/*
		 * The buffer implementation is not working or at least very much
		 * unintuitive. I'll implement direct access to the data until I've
		 * talked to the team. TODO: Fix this! // get the previous, current and
		 * next grid for displaying. Use null if we're at the start or end of an
		 * animation Grid prevGrid = (buffer.getPosition() > 0) ?
		 * buffer.getGrid(-1) : null; Grid grid = buffer.getCurrent(); Grid
		 * nextGrid = (buffer.getPosition() < buffer.getNumGrids() - 1) ?
		 * buffer.getNext() : null; if (prevGrid != null) {
		 * copyGridDataToPanel(prevGrid, prevLedPanel); } else {
		 * prevLedPanel.clear(); } copyGridDataToPanel(grid, ledPanel); if
		 * (nextGrid != null) { copyGridDataToPanel(nextGrid, nextLedPanel); }
		 * else { nextLedPanel.clear(); }
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
		optionsPanel.setOptions(buffer.getSpeed().getValue(), buffer.getDelay()
				.getValue(), buffer.getDirection().getValue());
		switchMode();
	}

	public void switchMode() {
		// treat text buffers different from graphics buffers
		if (bufferRef.getAnimationType() == AnimationType.TEXT) {
			ledPanel.setEnabled(false);
			editTextPanel.setVisible(true);
			virtualKeyboardPanel.setVisible(true);
			String text = ((TextDisplayBuffer) bufferRef).getText();
			// note: conditional to prevent the cursor jumping
			if (!editTextField.getText().equals(text)) {
				editTextField.setText(text);
			}
		} else {
			ledPanel.setEnabled(true);
			editTextPanel.setVisible(false);
			virtualKeyboardPanel.setVisible(false);
		}
		String rawString = bufferRef.getRawString();
		if (!rawInputTextField.getText().equals(rawString)) {
			rawInputTextField.setText(rawString);
		}
	}

	public void reset() {
		currentPosition = 0;
		optionsPanel.setPosition(currentPosition);
	}

	/**
	 * Switch our temporary DisplayBuffer to the original passed on startEdit
	 * The buffer must not be touched anymore after this because we switch
	 * them(!)
	 */
	public void saveBuffer() {
		HacklaceConfigManager cm = AnimatorGui.getInstance()
				.getHacklaceConfigManager();
		List<DisplayBuffer> list = cm.getList();
		list.set(list.indexOf(origBuffer), bufferRef);
		bufferRef = null;
		origBuffer = null;
		// refresh list on home page because it contains the text for
		// TextDisplayBuffers
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
		if (bufferRef == null)
			return;
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
		// System.out.println("LED Changed: " + row + "/" + column + " to " +
		// newValue);
		bufferRef.setValueAt(column + DisplayBuffer.COLUMNS * currentPosition,
				row, newValue);
		rawInputTextField.setText(bufferRef.getRawString());
	}
}
