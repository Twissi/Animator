/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.enums.StepWidth;
import org.hacklace.animator.gui.actions.LedObserver;
import org.hacklace.animator.gui.actions.OptionsObserver;
import org.hacklace.animator.gui.actions.PositionSlideObserver;
import org.hacklace.animator.gui.actions.PositionSliderChangeListener;
import org.hacklace.animator.gui.actions.RawInputFullLineApplyActionListener;
import org.hacklace.animator.gui.actions.RawInputRestOfLineApplyActionListener;
import org.hacklace.animator.gui.actions.SaveObserver;

public abstract class EditPanel extends JPanel implements LedObserver,
		OptionsObserver, SaveObserver, BufferReferencer, PositionSlideObserver {
	private static final long serialVersionUID = -5137928768652375360L;

	protected AnimationOptionsPanel optionsPanel;
	protected JPanel rawInputPanel;
	protected JTextField rawInputRestOfLineTextField;
	protected JTextField rawInputFullLineTextField;
	protected LedPanel ledPanel;
	protected LedPanel playPanel;
	protected JSlider positionSlider;
	protected JButton playButton;
	protected Thread playThread = null;

	private DisplayBuffer buffer = null; // our internal temporary displayBuffer
	// for editing
	protected DisplayBuffer origBuffer; // keep a reference to the original
										// buffer for overwriting on save
	protected int currentPosition = 0;

	private JTextArea errorArea;

	protected PositionSliderChangeListener positionSliderChangeListener;

	public static final int GRID_ROWS = AnimatorGui.getIniConf().rows();
	public static final int GRID_COLS = AnimatorGui.getIniConf().columns();
	public static final int NUM_GRIDS_TO_SHOW = 5;
	private static final String BUTTON_TEXT_PLAY = "Play";
	private static final String BUTTON_TEXT_STOP = "Stop";

	private HomePanel homePanel;

	protected AnimatorGui animatorGui;

	private HacklaceConfigManager configManager;

	public static EditPanel factory(DisplayBuffer displayBuffer,
			HomePanel homePanel, AnimatorGui animatorGui,
			HacklaceConfigManager configManager) {
		switch (displayBuffer.getAnimationType()) {
		case TEXT:
			return new EditTextPanel(displayBuffer, homePanel, animatorGui,
					configManager);
		case GRAPHIC:
			return new EditGraphicPanel(displayBuffer, homePanel, animatorGui,
					configManager);
		case REFERENCE:
			return new EditReferencePanel(displayBuffer, homePanel,
					animatorGui, configManager);
		case MIXED:
			return new EditMixedPanel(displayBuffer, homePanel, animatorGui,
					configManager);
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
	private static JLabel createDebugLabel(String text, Color color,
			int minWidth, int minHeight) {
		JLabel label = new JLabel(text);
		label.setBorder(BorderFactory.createLineBorder(Color.black));
		label.setBackground(color);
		label.setMinimumSize(new Dimension(minWidth, minHeight));
		return label;
	}

	protected EditPanel(DisplayBuffer displayBuffer, HomePanel homePanel,
			AnimatorGui animatorGui, HacklaceConfigManager configManager) {
		this.homePanel = homePanel;
		this.animatorGui = animatorGui;
		this.configManager = configManager;
		buffer = displayBuffer.clone();
		origBuffer = displayBuffer;
		// common components for all types of edit panels
		optionsPanel = new AnimationOptionsPanel((OptionsObserver) this,
				(SaveObserver) this, animatorGui);
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
		positionSlider = createPositionSlider();
		c.fill = GridBagConstraints.HORIZONTAL;
		add(positionSlider, c);
		c.fill = GridBagConstraints.NONE;
		JPanel editDetailsPanel = new JPanel();
		addMoreComponents(editDetailsPanel);
		add(editDetailsPanel, c);
		c.gridx = 0;
		c.gridwidth = 2;
		add(rawInputPanel, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 2;
		add(errorArea = new JTextArea(), c);
		errorArea.setEditable(false);
		// add play panel and button next to slider
		c.gridx = 2;
		c.gridy = 0;
		playPanel = new LedPanel(GRID_ROWS, GRID_COLS);
		playPanel.setEnabled(false);
		playPanel.showLabels(false);
		add(playPanel, c);
		playButton = new JButton(BUTTON_TEXT_PLAY);
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (playButton.getText() == BUTTON_TEXT_PLAY) {
					playButton.setText(BUTTON_TEXT_STOP);
					startPlaying();
				} else {
					playButton.setText(BUTTON_TEXT_PLAY);
					stopPlaying();
				}
			}
		});
		c.gridy = 1;
		add(playButton, c);
		// set options and data from the display buffer
		updateUiFromDisplayBuffer();
	}

	/**
	 * Overwrite this in children to add components on the right side above the
	 * raw edit panel
	 * 
	 * @param panel
	 */
	protected void addMoreComponents(JPanel panel) {
		// by default do nothing
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
		rawInputRestOfLineTextField = new JTextField(AnimatorGui.getIniConf()
				.getNumGrids());
		c.gridx = 1;
		rawInputPanel.add(rawInputRestOfLineTextField, c);
		JButton button = new JButton("Apply");
		button.addActionListener(new RawInputRestOfLineApplyActionListener(
				rawInputRestOfLineTextField, this, animatorGui));
		c.gridx = 2;
		rawInputPanel.add(button, c);

		label = new JLabel("Raw data with modus byte:");
		c.gridx = 0;
		c.gridy = line + 1;
		rawInputPanel.add(label, c);
		rawInputFullLineTextField = new JTextField(AnimatorGui.getIniConf()
				.getNumGrids() + 1);
		c.gridx = 1;
		rawInputPanel.add(rawInputFullLineTextField, c);
		button = new JButton("Apply");
		button.addActionListener(new RawInputFullLineApplyActionListener(
				rawInputFullLineTextField, this, animatorGui));
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
		JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 0,
				getMaximumGrid(), 0);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
		slider.setMinorTickSpacing(1);
		positionSliderChangeListener = new PositionSliderChangeListener(this,
				slider, this);
		slider.addChangeListener(positionSliderChangeListener);

		return slider;
	}

	protected int getMaximumGrid() {
		return AnimatorGui.getIniConf().getNumGrids() - NUM_GRIDS_TO_SHOW;
	}

	protected void copyBufferToLedPanel(int position, LedPanel panel) {
		for (int x = 0; x < panel.getNumCols(); x++) {
			for (int y = 0; y < panel.getNumRows(); y++) {
				panel.setLedFromBuffer(x, y,
						buffer.getValueAtColumnRow(x + GRID_COLS * position, y));
			}
		}
	}

	public void setNewDisplayBuffer(DisplayBuffer newBuffer) {
		this.buffer = newBuffer;
		updateUiFromDisplayBuffer();
	}

	public void updateUiFromDisplayBuffer() {
		optionsPanel.setOptions(buffer.getSpeed(), buffer.getDelay(),
				buffer.getDirection(), buffer.getStepWidth());
		updateRawTextFields();
		copyBufferToLedPanel(currentPosition, ledPanel);
		onStepChanged(buffer.getStepWidth());
	}

	protected void updateRawTextFields() {
		ErrorContainer errorContainer = new ErrorContainer();
		FullConfigLine fullLine = buffer.getFullConfigLine();
		rawInputFullLineTextField.setText(fullLine.getOriginalString());
		rawInputRestOfLineTextField.setText(fullLine.getRestOfLine(
				errorContainer).getModifiedRawString());
		updateRawDataDirectModeTextField(fullLine);
		buffer.additionalChecks(errorContainer);
		showErrors(errorContainer);
	}

	@Override
	public void showErrors(ErrorContainer errorContainer) {
		errorArea.setText(errorContainer.toString());
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
		// by default do nothing
	}

	/**
	 * Switch our temporary DisplayBuffer to the original passed on startEdit
	 * The buffer must not be touched anymore after this because we switch
	 * them(!)
	 */
	public void saveBuffer() {
		List<DisplayBuffer> list = configManager.getList();
		int origBufferIndex = list.indexOf(origBuffer);
		if (origBufferIndex == -1) {
			// the buffer could not be found. This means, it has been replaced
			// during editing (see issue #91). We'll replace it for an easy fix
			// until we have refactored all the other stuff
			origBufferIndex = homePanel.getSelectedIndex();
		}
		list.set(origBufferIndex, buffer);
		// null the buffer references - we rather have a NullpointerException
		// than editing the wrong data!
		buffer = null;
		origBuffer = null;
		// refresh list on home page because it contains the text for
		// TextDisplayBuffers
		homePanel.updateList(configManager.getList(), true);
	}

	@Override
	public void onSpeedChanged(Speed newSpeed) {
		buffer.setSpeed(newSpeed);
		updateRawTextFields();
	}

	@Override
	public void onDelayChanged(Delay newDelay) {
		buffer.setDelay(newDelay);
		updateRawTextFields();
	}

	@Override
	public void onDirectionChanged(Direction newDirection) {
		buffer.setDirection(newDirection);
		updateRawTextFields();
	}

	@Override
	public void onStepChanged(StepWidth newStepWidth) {
		buffer.setStepWidth(newStepWidth);
		ledPanel.setSpacing(newStepWidth == StepWidth.FIVE);
		ledPanel.showLabels(newStepWidth == StepWidth.FIVE);
		updateRawTextFields();
	}

	public DisplayBuffer getDisplayBuffer() {
		return buffer;
	}

	@Override
	public void onLedChange(int column, int row, boolean newValue) {
		// some subclasses don't need this method so do nothing
	}

	protected void startPlaying() {
		playThread = new Thread(new AnimatorRunnable(buffer, playPanel));
		playThread.start();
	}

	protected void stopPlaying() {
		if (playThread != null) {
			playThread.interrupt();
			playThread = null;
		}
	}

	public void close() {
		stopPlaying();
	}

	@Override
	public DisplayBuffer getBuffer() {
		return buffer;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * called when moving the position slider
	 */
	@Override
	public void onPositionChanged(int newPosition) {
		this.currentPosition = newPosition;
		updateUiFromDisplayBuffer();
		ledPanel.setOffset(currentPosition);
	}

	@Override
	public void onSaveAnimation() {
		ErrorContainer errorContainer = new ErrorContainer();

		boolean isSaveAble = this.getBuffer().isSaveable(errorContainer);

		if (isSaveAble) {
			this.saveBuffer();
			animatorGui.endEditMode();
		} else {
			this.showErrors(errorContainer);
			JOptionPane.showMessageDialog(null, "The animation cannot be saved because there are errors. \n(See log at the bottom of the screen.)",
					"Error saving", JOptionPane.ERROR_MESSAGE);

		}
	}

}
