package org.hacklace.animator.gui;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.FontUtil;
import org.hacklace.animator.displaybuffer.TextDisplayBuffer;

public class EditTextPanel extends EditPanel {

	private static final long serialVersionUID = 8568315594127340767L;

	private LedPanel textLedPanel; // display for text
	private JTextField textEditField;
	private JPanel textPanel;
	private JPanel virtualKeyboardPanel;

	public EditTextPanel(DisplayBuffer displayBuffer) {
		super(displayBuffer);
		/** @TODO implement!!! */
	}

	@Override
	protected void addMoreComponents(JPanel panel) {
		virtualKeyboardPanel = createVirtualKeyboardPanel();
		panel.add(virtualKeyboardPanel);
		textPanel = createTextPanel();
		panel.add(textPanel);
	}

	private JPanel createVirtualKeyboardPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		panel.add(new JLabel("Virtual Keyboard:"));
		ActionListener virtualKeyboardListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				VirtualKeyboardButton vk = (VirtualKeyboardButton) arg0
						.getSource();
				String chars = vk.getString();
				int pos = textEditField.getCaretPosition();
				try {
					textEditField.getDocument().insertString(pos, chars, null);
					((TextDisplayBuffer) bufferRef).setText(textEditField
							.getText());
					setFromDisplayBuffer(bufferRef);
				} catch (BadLocationException e) {
					// just do nothing, this should not happen anyways
				}
			}
		};
		final int VIRTUAL_KEYS_PER_ROW = 15;
		int columsRemaining = VIRTUAL_KEYS_PER_ROW;

		JPanel keyboardRow = new JPanel();
		panel.add(keyboardRow);

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
				panel.add(keyboardRow);
				columsRemaining = VIRTUAL_KEYS_PER_ROW;
			}
			columsRemaining--;

			button = new VirtualKeyboardButton(i);
			if (i - FontUtil.SPECIAL_CHAR_OFFSET == '^')
				button.setToolTipText("six empty columns");
			button.addActionListener(virtualKeyboardListener);
			keyboardRow.add(button);
		}
		return panel;
	}

	private JPanel createTextPanel() {
		textPanel = new JPanel();
		textEditField = new JTextField(DisplayBuffer.getNumGrids());
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
		textEditField.setDocument(doc);
		textEditField.addKeyListener(new KeyListener() {
			private void updateText() {
				((TextDisplayBuffer) bufferRef).setText(textEditField.getText());
				setFromDisplayBuffer(bufferRef);
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
		textPanel.add(textEditField);
		return textPanel;
	}

	public void setFromDisplayBuffer(DisplayBuffer buffer)	{
		copyBufferToPanel(currentPosition, textLedPanel);
	}
}
