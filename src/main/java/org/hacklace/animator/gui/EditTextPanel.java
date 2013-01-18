package org.hacklace.animator.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.FontUtil;
import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.TextDisplayBuffer;

public class EditTextPanel extends EditPanel {

	private static final long serialVersionUID = 8568315594127340767L;

	private JTextField textEditField;
	private JPanel textPanel;
	private JPanel virtualKeyboardPanel;

	public EditTextPanel(DisplayBuffer displayBuffer, HomePanel homePanel, AnimatorGui animatorGui, HacklaceConfigManager configManager) {
		super(displayBuffer, homePanel, animatorGui, configManager);
		ledPanel.setEnabled(false);
		TextDisplayBuffer textDisplayBuffer = (TextDisplayBuffer) getBuffer();
		String text = textDisplayBuffer.getText();
		textEditField.setText(text);
	}

	@Override
	protected void addMoreComponents(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		virtualKeyboardPanel = createVirtualKeyboardPanel();
		panel.add(virtualKeyboardPanel, c);
		textPanel = createTextPanel();
		panel.add(textPanel, c);
	}

	private JPanel createVirtualKeyboardPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		panel.add(new JLabel(
				"Hover over \"empty\" buttons to see the width of the space."));
		ActionListener virtualKeyboardListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				VirtualKeyboardButton vk = (VirtualKeyboardButton) event
						.getSource();
				String chars = vk.getString();
				int pos = textEditField.getCaretPosition();
				try {
					ErrorContainer errorContainer = new ErrorContainer();
					textEditField.getDocument().insertString(pos, chars, null);
					((TextDisplayBuffer) getBuffer()).setText(
							textEditField.getText(), errorContainer);
					updateUiFromDisplayBuffer();
					showErrors(errorContainer);
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
		button.setToolTipText("1 empty column");
		button.addActionListener(virtualKeyboardListener);
		keyboardRow.add(button);
		columsRemaining--;

		button = new VirtualKeyboardButton('^');
		button.setToolTipText("^ needs to be escaped to ^^");
		button.addActionListener(virtualKeyboardListener);
		keyboardRow.add(button);
		columsRemaining--;

		button = new VirtualKeyboardButton('$');
		button.setToolTipText("$ needs to be escaped to $$");
		button.addActionListener(virtualKeyboardListener);
		keyboardRow.add(button);
		columsRemaining--;

		button = new VirtualKeyboardButton('~');
		button.addActionListener(virtualKeyboardListener);
		button.setToolTipText("~ needs to be escaped to ~~");
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
				button.setToolTipText("6 empty columns");
			button.addActionListener(virtualKeyboardListener);
			keyboardRow.add(button);
		}
		return panel;
	}

	private JPanel createTextPanel() {
		textPanel = new JPanel();
		final int maxCols = AnimatorGui.getIniConf().maxColumns();
		textEditField = new JTextField(40);
		PlainDocument doc = new PlainDocument();
		doc.setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offs, String str,
					AttributeSet a) throws BadLocationException {
				ErrorContainer errorContainer = new ErrorContainer();
				int newLength = FontUtil.getWidthForRawString(fb.getDocument()
						.getText(0, fb.getDocument().getLength()),
						errorContainer)
						+ FontUtil.getWidthForRawString(str, errorContainer);
				if (newLength <= maxCols)
					super.insertString(fb, offs, str, a);
				else
					Toolkit.getDefaultToolkit().beep();
			}

			@Override
			public void replace(FilterBypass fb, int offs, int length,
					String str, AttributeSet a) throws BadLocationException {
				ErrorContainer errorContainer = new ErrorContainer();
				int newLength = FontUtil.getWidthForRawString(fb.getDocument()
						.getText(0, fb.getDocument().getLength()),
						errorContainer)
						+ FontUtil.getWidthForRawString(str, errorContainer)
						- length;
				if (newLength <= maxCols)
					super.replace(fb, offs, length, str, a);
				else
					Toolkit.getDefaultToolkit().beep();
			}
		});
		textEditField.setDocument(doc);
		textEditField.addKeyListener(new KeyAdapter() {
			private void updateText() {
				ErrorContainer errorContainer = new ErrorContainer();
				((TextDisplayBuffer) getBuffer()).setText(
						textEditField.getText(), errorContainer);
				updateUiFromDisplayBuffer();
				showErrors(errorContainer);
			}

			@Override
			public void keyReleased(KeyEvent event) {
				updateText();
			}

		});
		textPanel.add(textEditField);
		return textPanel;
	}

	@Override
	public void onRawTextChanged() {
		textEditField.setText(getBuffer().getText());
	}

	@Override
	public TextDisplayBuffer getBuffer() {
		return (TextDisplayBuffer) super.getBuffer();
	}

}
