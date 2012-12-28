package org.hacklace.animator.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hacklace.animator.displaybuffer.DisplayBuffer;

public class EditGraphicPanel extends EditPanel implements LedObserver {

	private static final long serialVersionUID = -8224406641046738423L;

	private JPanel ledPanelPanel;
	private LedPanel prevLedPanel; // display of the previous frame
	private LedPanel currentLedPanel; // display/edit of the current frame
	private LedPanel nextLedPanel; // display of the next frame
	private JSlider positionSlider;
	private JLabel prevLabel;
	private JLabel currentLabel;
	private JLabel nextLabel;

	public EditGraphicPanel(DisplayBuffer displayBuffer) {
		super(displayBuffer);
		/** @TODO implement!!! */
	}

	@Override
	protected void addMoreComponents(JPanel panel) {
		ledPanelPanel = createLedPanelPanel();
		panel.add(ledPanelPanel);
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
				setFromDisplayBuffer(bufferRef);
			}
		});
		ledPanelPanel.add(positionSlider, c);
		return ledPanelPanel;
	}

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
		if (currentPosition > 0) {
			copyBufferToPanel(currentPosition - 1, prevLedPanel);
			prevLabel.setText(Integer.toString(currentPosition - 1));
		} else {
			prevLedPanel.clear();
			prevLabel.setText("-");
		}
		copyBufferToPanel(currentPosition, currentLedPanel);
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
	}

	public void onLedChange(int row, int column, boolean newValue) {
		// System.out.println("LED Changed: " + row + "/" + column + " to " +
		// newValue);
		bufferRef
				.setValueAt(column + gridCols * currentPosition, row, newValue);
		rawInputTextField.setText(bufferRef.getRawString());
	}

}
