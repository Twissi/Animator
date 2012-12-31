package org.hacklace.animator.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.hacklace.animator.IniConf;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.enums.StepWidth;
import org.hacklace.animator.gui.actions.RawInputDirectModeApplyActionListener;

public class EditGraphicPanel extends EditPanel implements LedObserver {

	private static final long serialVersionUID = -8224406641046738423L;

	private JPanel ledPanelPanel;
	private GridBagLayout ledPanelPanelLayout;
	private LedPanel prevLedPanel; // display of the previous frame
	private LedPanel currentLedPanel; // display/edit of the current frame
	private LedPanel nextLedPanel; // display of the next frame
	private JLabel prevLabel;
	private JLabel currentLabel;
	private JLabel nextLabel;
	private JTextField rawInputDirectModeTextField;

	public EditGraphicPanel(DisplayBuffer displayBuffer) {
		super(displayBuffer);
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
		rawInputDirectModeTextField.setText(fullLine.getRestOfLine()
				.getDirectMode().getValue());
	}

	@Override
	protected void addMoreComponents(JPanel panel) {
		ledPanelPanel = createLedPanelPanel();
		panel.add(ledPanelPanel);
	}

	private void setGridSpacing(boolean isSpaced) {
		GridBagConstraints c = new GridBagConstraints();
		if (isSpaced) {
			c.insets = new Insets(5, 5, 5, 5);
		} else {
			c.insets = new Insets(0, 0, 0, 0);
		}
		ledPanelPanelLayout.setConstraints(prevLedPanel, c);
		ledPanelPanelLayout.setConstraints(currentLedPanel, c);
		ledPanelPanelLayout.setConstraints(nextLedPanel, c);
		ledPanelPanel.revalidate();
		ledPanelPanel.repaint();
	}

	@Override
	public void onStepChanged(StepWidth newStep) {
		super.onStepChanged(newStep);
		setGridSpacing(newStep == StepWidth.FIVE);
	}

	/**
	 * Generate the panel of LedPanels for the edit view
	 * 
	 * @return
	 */
	private JPanel createLedPanelPanel() {
		JPanel ledPanelPanel = new JPanel();
		ledPanelPanelLayout = new GridBagLayout();
		ledPanelPanel.setLayout(ledPanelPanelLayout);
		GridBagConstraints c = new GridBagConstraints();
		// first row: 3 LedPanels
		c.insets = new Insets(5, 5, 5, 5);
		prevLedPanel = new LedPanel(gridRows, gridCols);
		prevLedPanel.setEnabled(false);
		c.gridx = 0;
		ledPanelPanel.add(prevLedPanel, c);
		currentLedPanel = new LedPanel(gridRows, gridCols);
		currentLedPanel.addObserver(this);
		c.gridx = 1;
		ledPanelPanel.add(currentLedPanel, c);
		nextLedPanel = new LedPanel(gridRows, gridCols);
		nextLedPanel.setEnabled(false);
		c.gridx = 2;
		ledPanelPanel.add(nextLedPanel, c);
		// next row: 3 labels for frame number
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
		// next row: One slider across all columns
		c.gridy = 2;
		c.gridx = 0;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		JSlider positionSlider = createPositionSlider();
		ledPanelPanel.add(positionSlider, c);
		return ledPanelPanel;
	}

	@Override
	public void setFromDisplayBuffer(DisplayBuffer buffer) {
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
		super.setFromDisplayBuffer(buffer);
		if (currentPosition > 0) {
			copyBufferToPanel(currentPosition - 1, prevLedPanel);
			prevLabel.setText(Integer.toString(currentPosition - 1));
		} else {
			prevLedPanel.clear();
			prevLabel.setText("-");
		}
		copyBufferToPanel(currentPosition, currentLedPanel);
		currentLabel.setText(Integer.toString(currentPosition));
		if (currentPosition < IniConf.getInstance().getNumGrids() - 1) {
			copyBufferToPanel(currentPosition + 1, nextLedPanel);
			nextLabel.setText(Integer.toString(currentPosition + 1));
		} else {
			nextLedPanel.clear();
			nextLabel.setText("-");
		}
	}

	public void onLedChange(int row, int column, boolean newValue) {
		// System.out.println("LED Changed: " + row + "/" + column + " to " +
		// newValue);
		buffer.setValueAt(column + gridCols * currentPosition, row, newValue);
		updateRawTextFields();
	}

}
