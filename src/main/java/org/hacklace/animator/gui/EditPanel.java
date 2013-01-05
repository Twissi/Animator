package org.hacklace.animator.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.enums.StepWidth;
import org.hacklace.animator.gui.actions.RawInputFullLineApplyActionListener;
import org.hacklace.animator.gui.actions.RawInputRestOfLineApplyActionListener;

public abstract class EditPanel extends JPanel implements OptionsObserver, LedObserver {
	private static final long serialVersionUID = -5137928768652375360L;

	protected AnimationOptionsPanel optionsPanel;
	protected JPanel rawInputPanel;
	protected JTextField rawInputRestOfLineTextField;
	protected JTextField rawInputFullLineTextField;
	protected LedPanel ledPanel;

	protected DisplayBuffer buffer; // our internal temporary displayBuffer
									// for editing
	protected DisplayBuffer origBuffer; // keep a reference to the original
										// buffer for overwriting on save
	protected int currentPosition = 0;

	public static final int GRID_ROWS = IniConf.getInstance().rows();
	public static final int GRID_COLS = IniConf.getInstance().columns();
	public static final int NUM_GRIDS_TO_SHOW = 5;

	public static EditPanel factory(DisplayBuffer displayBuffer) {
		switch (displayBuffer.getAnimationType()) {
		case TEXT:
			return new EditTextPanel(displayBuffer);
		case GRAPHIC:
			return new EditGraphicPanel(displayBuffer);
		case REFERENCE:
			return new EditReferencePanel(displayBuffer);
		case MIXED:
			return new EditMixedPanel(displayBuffer);
		}
		return null;
	}

	/**
	 * This is a helper function for developing layouts. It doesn't work though,
	 * if you want to use it you have to fix the label so it actually uses
	 * minWidth/height
	 * 
	 * @param text
	 * @param color
	 * @param minWidth
	 * @param minHeight
	 * @return
	 */
	@SuppressWarnings("unused")
	private JLabel createDebugLabel(String text, Color color, int minWidth,
			int minHeight) {
		JLabel label = new JLabel(text);
		label.setBorder(BorderFactory.createLineBorder(Color.black));
		label.setBackground(color);
		label.setMinimumSize(new Dimension(minWidth, minHeight));
		return label;
	}

	protected EditPanel(DisplayBuffer displayBuffer) {
		buffer = displayBuffer.clone();
		origBuffer = displayBuffer;
		// common components for all types of edit panels
		optionsPanel = new AnimationOptionsPanel();
		rawInputPanel = createRawInputPanel();

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(5, 5, 5, 5);
		// Left side: Options panel, spanning all rows at 0,0
		c.gridheight = 4;
		add(optionsPanel, c);
		// Right side
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = GridBagConstraints.RELATIVE;
		ledPanel = new LedPanel(GRID_ROWS, GRID_COLS * NUM_GRIDS_TO_SHOW);
		ledPanel.addObserver(this);
		add(ledPanel, c);
		JSlider positionSlider = createPositionSlider();
		c.fill = GridBagConstraints.HORIZONTAL;
		add(positionSlider, c);
		c.fill = GridBagConstraints.NONE;
		JPanel editDetailsPanel = new JPanel();
		addMoreComponents(editDetailsPanel);
		add(editDetailsPanel, c);
		c.gridx = 0;
		c.gridwidth = 2;
		add(rawInputPanel, c);
		optionsPanel.addObserver(this);
		// set options and data from the display buffer
		setFromDisplayBuffer(buffer);
	}

	/**
	 * Overwrite this in children to add components on the right side above the
	 * raw edit panel
	 */
	protected void addMoreComponents(JPanel panel) {
	}

	/**
	 * overwrite this only in the graphic panel
	 * 
	 * @param rawInputPanel
	 * @param c
	 * @return the line number with which to continue (0 in general, 1 for
	 *         graphic)
	 */
	protected int createRawDataDirectModeElements(JPanel rawInputPanel,
			GridBagConstraints c) {
		return 0;
	}

	protected JPanel createRawInputPanel() {
		JPanel rawInputPanel = new JPanel();

		rawInputPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;

		int line = createRawDataDirectModeElements(rawInputPanel, c);

		JLabel label = new JLabel("Raw data without modus byte:");
		c.gridx = 0;
		c.gridy = line;
		rawInputPanel.add(label, c);
		rawInputRestOfLineTextField = new JTextField(IniConf.getInstance()
				.getNumGrids());
		c.gridx = 1;
		rawInputPanel.add(rawInputRestOfLineTextField, c);
		JButton button = new JButton("Apply");
		button.addActionListener(new RawInputRestOfLineApplyActionListener(
				rawInputRestOfLineTextField, this));
		c.gridx = 2;
		rawInputPanel.add(button, c);

		label = new JLabel("Raw data with modus byte:");
		c.gridx = 0;
		c.gridy = line + 1;
		rawInputPanel.add(label, c);
		rawInputFullLineTextField = new JTextField(IniConf.getInstance()
				.getNumGrids() + 1);
		c.gridx = 1;
		rawInputPanel.add(rawInputFullLineTextField, c);
		button = new JButton("Apply");
		button.addActionListener(new RawInputFullLineApplyActionListener(
				rawInputFullLineTextField, this));
		c.gridx = 2;
		rawInputPanel.add(button, c);

		return rawInputPanel;
	}

	/**
	 * The position slider is not created automatically. Any child using it must
	 * call this function and add the slider somewhere in its ui.
	 * 
	 * It is implemented here because its functionality is always the same.
	 * 
	 * @return
	 */
	protected JSlider createPositionSlider() {
		JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 0, getMaximumGrid() - NUM_GRIDS_TO_SHOW + 1, 0);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
		slider.setMinorTickSpacing(1);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				// ignore the event if we don't have a valid buffer yet
				if (buffer == null)
					return;
				currentPosition = ((JSlider) arg0.getSource()).getValue();
				setFromDisplayBuffer(buffer);
				ledPanel.setOffset(currentPosition);
			}
		});
		return slider;
	}
	
	protected int getMaximumGrid() {
		return IniConf.getInstance().getNumGrids() - 1;
	}

	/*
	 * see note below... public void copyGridDataToPanel(Grid grid, LedPanel
	 * panel) { for (int x=0; x<DisplayBuffer.COLUMNS; x++) { for (int y=0;
	 * y<DisplayBuffer.ROWS; y++) { panel.setLed(y, x, grid.getData()[x][y]); }
	 * } }
	 */

	protected void copyBufferToPanel(int position, LedPanel panel) {
		for (int x = 0; x < panel.getCols(); x++) {
			for (int y = 0; y < panel.getRows(); y++) {
				panel.setLed(y, x,
						buffer.getValueAt(x + GRID_COLS * position, y));
			}
		}
	}

	public void setFromDisplayBuffer(DisplayBuffer buffer) {
		this.buffer = buffer;
		optionsPanel.setOptions(buffer.getSpeed(), buffer.getDelay(),
				buffer.getDirection(), buffer.getStepWidth());
		updateRawTextFields();
	}

	protected void updateRawTextFields() {
		FullConfigLine fullLine = buffer.getRawString();
		rawInputFullLineTextField.setText(fullLine.getValue());
		rawInputRestOfLineTextField
				.setText(fullLine.getRestOfLine().getValue());
		updateRawDataDirectModeTextField(fullLine);
	}

	/**
	 * only overwrite for graphic animations (does nothing otherwise)
	 * 
	 * @param fullLine
	 */
	protected void updateRawDataDirectModeTextField(FullConfigLine fullLine) {
		// do nothing
	}

	/**
	 * override this if you want to react on a change of the raw text
	 */
	public void onRawTextChanged() {
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
		list.set(list.indexOf(origBuffer), buffer);
		// null the buffer references - we rather have a NullpointerException
		// than editing the wrong data!
		buffer = null;
		origBuffer = null;
		// refresh list on home page because it contains the text for
		// TextDisplayBuffers
		AnimatorGui.getInstance().getHomePanel().updateList(cm.getList(), true);
	}

	public void onSpeedChanged(Speed newSpeed) {
		buffer.setSpeed(newSpeed);
		updateRawTextFields();
	}

	public void onDelayChanged(Delay newDelay) {
		buffer.setDelay(newDelay);
		updateRawTextFields();
	}

	public void onDirectionChanged(Direction newDirection) {
		buffer.setDirection(newDirection);
		updateRawTextFields();
	}

	public void onStepChanged(StepWidth newStep) {
		buffer.setStepWidth(newStep);
		ledPanel.setSpacing(newStep == StepWidth.FIVE);
		ledPanel.showLabels(newStep == StepWidth.FIVE);
		updateRawTextFields();
	}

	public boolean onSaveAnimation() {
		if (!buffer.isSaveable())
			return false; // cannot save
		saveBuffer();
		return true; // saved
	}

	public DisplayBuffer getDisplayBuffer() {
		return buffer;
	}

	public void onLedChange(int row, int column, boolean newValue) {
	}
}
