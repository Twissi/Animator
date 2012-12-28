package org.hacklace.animator.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.IniConf;
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

	private AnimationOptionsPanel optionsPanel;
	private JPanel ledPanelPanel;
	private JPanel editTextPanel;
	private JPanel virtualKeyboardPanel;
	private JPanel rawInputPanel;
	private JTextField editTextField;
	private JTextField rawInputTextField;
	private LedPanel prevLedPanel; // display of the previous frame
	private LedPanel ledPanel; // display/edit of the current frame
	private LedPanel nextLedPanel; // display of the next frame
	private JSlider positionSlider;
	private JLabel prevLabel;
	private JLabel currentLabel;
	private JLabel nextLabel;
	private DisplayBuffer bufferRef; // our internal temporary displayBuffer for
										// editing
	private DisplayBuffer origBuffer; // keep a reference to the original buffer
										// for overwriting on save
	private int currentPosition = 0;

	private int gridRows = IniConf.getInstance().rows();
	private int gridCols = IniConf.getInstance().columns();

	public EditAnimationPanel() {
		ledPanelPanel = createLedPanelPanel();
		editTextPanel = createEditTextPanel();
		createVirtualKeyboardPanel();
		rawInputPanel = createRawInputPanel();
		optionsPanel = new AnimationOptionsPanel();

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(5, 5, 5, 5);
		// Left side: Options panel, spanning all rows at 0,0
		c.gridheight = GridBagConstraints.REMAINDER;
		add(optionsPanel, c);
		// Right side, 4 rows, no spans
		c.gridheight = 1;
		c.gridx = 1;
		add(ledPanelPanel, c);
		c.gridy = GridBagConstraints.RELATIVE;
		add(editTextPanel, c);
		add(virtualKeyboardPanel, c);
		add(rawInputPanel, c);
		reset();
		optionsPanel.addObserver(this);
	}

	private void createVirtualKeyboardPanel() {
		virtualKeyboardPanel = new JPanel();
		virtualKeyboardPanel.setLayout(new GridLayout(0, 1));
		virtualKeyboardPanel.add(new JLabel("Virtual Keyboard:"));
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
		final int VIRTUAL_KEYS_PER_ROW = 15;
		int columsRemaining = VIRTUAL_KEYS_PER_ROW;

		JPanel keyboardRow = new JPanel();
		virtualKeyboardPanel.add(keyboardRow);

		VirtualKeyboardButton button = new VirtualKeyboardButton(0x7F);
		button.setToolTipText("one empty column");
		button.addActionListener(virtualKeyboardListener);
		keyboardRow.add(button);
		columsRemaining--;

		button = new VirtualKeyboardButton('^');
		button.addActionListener(virtualKeyboardListener);
		keyboardRow.add(button);
		columsRemaining--;

		button = new VirtualKeyboardButton('$');
		button.addActionListener(virtualKeyboardListener);
		keyboardRow.add(button);
		columsRemaining--;

		button = new VirtualKeyboardButton('~');
		button.addActionListener(virtualKeyboardListener);
		keyboardRow.add(button);
		columsRemaining--;

		for (int i = FontUtil.LOWEST_SPECIAL_INDEX; i <= FontUtil.HIGHEST_INDEX; i++) {
			if (columsRemaining == 0) {
				keyboardRow = new JPanel();
				virtualKeyboardPanel.add(keyboardRow);
				columsRemaining = VIRTUAL_KEYS_PER_ROW;
			}
			columsRemaining--;

			button = new VirtualKeyboardButton(i);
			if (i - FontUtil.SPECIAL_CHAR_OFFSET == '^')
				button.setToolTipText("six empty columns");
			button.addActionListener(virtualKeyboardListener);
			keyboardRow.add(button);
		}
	}

	/**
	 * Generate the panel of LedPanels for the edit view
	 * 
	 * @return
	 */
	private JPanel createLedPanelPanel() {
		JPanel ledPanelPanel = new JPanel();
		ledPanelPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// first row: 3 LedPanels
		c.insets = new Insets(5, 5, 5, 5);
		prevLedPanel = new LedPanel(gridRows, gridCols);
		prevLedPanel.setEnabled(false);
		c.gridx = 0;
		ledPanelPanel.add(prevLedPanel, c);
		ledPanel = new LedPanel(gridRows, gridCols);
		ledPanel.addObserver(this);
		c.gridx = 1;
		ledPanelPanel.add(ledPanel, c);
		nextLedPanel = new LedPanel(gridRows, gridCols);
		nextLedPanel.setEnabled(false);
		c.gridx = 2;
		ledPanelPanel.add(nextLedPanel, c);
		// second row: 3 labels for frame number
		c.gridy = 1;
		c.gridx = 0;
		prevLabel = new JLabel("-", null, SwingConstants.CENTER);
		ledPanelPanel.add(prevLabel, c);
		currentLabel = new JLabel("-", null, SwingConstants.CENTER);
		c.gridx = 1;
		ledPanelPanel.add(currentLabel, c);
		nextLabel = new JLabel("-", null, SwingConstants.CENTER);
		c.gridx = 2;
		ledPanelPanel.add(nextLabel, c);
		// third row: One slider across all columns
		c.gridy = 2;
		c.gridx = 0;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		positionSlider = new JSlider(SwingConstants.HORIZONTAL, 1);
		positionSlider.setPaintTicks(true);
		positionSlider.setSnapToTicks(true);
		positionSlider.setMinorTickSpacing(1);
		positionSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				// ignore the event if we don't have a valid buffer yet
				if (bufferRef == null)
					return;
				currentPosition = ((JSlider) arg0.getSource()).getValue();
				setFromDisplayBuffer(bufferRef, false);
			}
		});
		ledPanelPanel.add(positionSlider, c);
		return ledPanelPanel;
	}

	private JPanel createEditTextPanel() {
		editTextPanel = new JPanel();
		editTextField = new JTextField(DisplayBuffer.getNumGrids());
		PlainDocument doc = new PlainDocument();
		doc.setDocumentFilter(new DocumentFilter() {
			public void insertString(FilterBypass fb, int offs, String str,
					AttributeSet a) throws BadLocationException {
				if ((fb.getDocument().getLength() + str.length()) <= DisplayBuffer
						.getNumGrids())
					super.insertString(fb, offs, str, a);
				else
					Toolkit.getDefaultToolkit().beep();
			}

			public void replace(FilterBypass fb, int offs, int length,
					String str, AttributeSet a) throws BadLocationException {
				if ((fb.getDocument().getLength() + str.length() - length) <= DisplayBuffer
						.getNumGrids())
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
		for (int x = 0; x < gridCols; x++) {
			for (int y = 0; y < gridRows; y++) {
				panel.setLed(y, x,
						bufferRef.getValueAt(x + gridCols * position, y));
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
		setPosition(currentPosition);
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

	public void onSpeedChanged(Speed newSpeed) {
		bufferRef.setSpeed(newSpeed);
	}

	public void onDelayChanged(Delay newDelay) {
		bufferRef.setDelay(newDelay);
	}

	public void onDirectionChanged(Direction newDirection) {
		bufferRef.setDirection(newDirection);
	}

	public void onSaveAnimation() {
		saveBuffer();
		AnimatorGui.getInstance().setEditMode(false);
	}

	public void onLedChange(int row, int column, boolean newValue) {
		// System.out.println("LED Changed: " + row + "/" + column + " to " +
		// newValue);
		bufferRef
				.setValueAt(column + gridCols * currentPosition, row, newValue);
		rawInputTextField.setText(bufferRef.getRawString());
	}

	public void setPosition(int position) {
		positionSlider.setValue(position);
	}

	public void setMaxPosition(int maxPosition) {
		positionSlider.setMaximum(maxPosition);
	}

}
