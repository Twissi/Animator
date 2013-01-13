package org.hacklace.animator.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;
import org.hacklace.animator.enums.StepWidth;
import org.hacklace.animator.gui.actions.CopyAndInsertFrameActionListener;
import org.hacklace.animator.gui.actions.CopyAndReplaceFrameActionListener;
import org.hacklace.animator.gui.actions.DeleteFrameActionListener;
import org.hacklace.animator.gui.actions.RawInputDirectModeApplyActionListener;

public class EditGraphicPanel extends EditPanel implements LedObserver {

	private static final long serialVersionUID = -8224406641046738423L;

	private JTextField rawInputDirectModeTextField;

	private JButton copyAndReplaceFrameButton;

	private JButton copyAndInsertFrameButton;

	private JButton deleteFrameButton;

	public EditGraphicPanel(DisplayBuffer displayBuffer) {
		super(displayBuffer);
	}

	@Override
	protected void addMoreComponents(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;

		copyAndReplaceFrameButton = new JButton(
				"Copy frame n to n+1 and replace");
		panel.add(copyAndReplaceFrameButton, c);
		copyAndReplaceFrameButton
				.addActionListener(new CopyAndReplaceFrameActionListener(this));

		copyAndInsertFrameButton = new JButton(
				"Copy frame n to n+1 and move frames right");
		panel.add(copyAndInsertFrameButton, c);
		copyAndInsertFrameButton
				.addActionListener(new CopyAndInsertFrameActionListener(this));

		deleteFrameButton = new JButton("Delete frame n and move frames left");
		panel.add(deleteFrameButton, c);
		deleteFrameButton
				.addActionListener(new DeleteFrameActionListener(this));

	}

	@Override
	public void onStepChanged(StepWidth newStep) {
		super.onStepChanged(newStep);
		enableCopyDeleteFrameButtons(newStep == StepWidth.FIVE);
	}

	private void enableCopyDeleteFrameButtons(boolean b) {
		copyAndReplaceFrameButton.setEnabled(b);
		copyAndInsertFrameButton.setEnabled(b);
		deleteFrameButton.setEnabled(b);
	}

	@Override
	protected int createRawDataDirectModeElements(JPanel rawInputPanel,
			GridBagConstraints c) {
		JLabel label = new JLabel("Direct mode raw data (no $FF):");
		rawInputPanel.add(label, c);
		rawInputDirectModeTextField = new JTextField(IniConf.getInstance()
				.getNumGrids() - 2);
		c.gridx = 1;
		rawInputPanel.add(rawInputDirectModeTextField, c);
		JButton button = new JButton("Apply");
		button.addActionListener(new RawInputDirectModeApplyActionListener(
				rawInputDirectModeTextField, this));
		c.gridx = 2;
		rawInputPanel.add(button, c);

		return 1;
	}

	@Override
	protected void updateRawDataDirectModeTextField(FullConfigLine fullLine) {
		rawInputDirectModeTextField
				.setText(fullLine.getRestOfLine(new ErrorContainer())
						.getDirectMode().getValue());
		// no need to actually get the errors, as they have already been
		// retrieved for the other raw data field
	}

	@Override
	public void onLedChange(int column, int row, boolean newValue) {
		getBuffer().setValueAtColumnRow(column + GRID_COLS * currentPosition,
				row, newValue);
		updateRawTextFields();
	}

	@Override
	public GraphicDisplayBuffer getBuffer() {
		return (GraphicDisplayBuffer) super.getBuffer();
	}
}
