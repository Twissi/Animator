package org.hacklace.animator.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;

public abstract class EditPanel extends JPanel implements OptionsObserver {
	private static final long serialVersionUID = -5137928768652375360L;

	protected AnimationOptionsPanel optionsPanel;
	protected JPanel rawInputPanel;
	
	protected JTextField rawInputTextField;
	
	protected DisplayBuffer bufferRef; // our internal temporary displayBuffer for
										// editing
	protected DisplayBuffer origBuffer; // keep a reference to the original buffer
										// for overwriting on save
	protected int currentPosition = 0;

	protected int gridRows = IniConf.getInstance().rows();
	protected int gridCols = IniConf.getInstance().columns();
	
	protected GridBagConstraints c = new GridBagConstraints();

	public static EditPanel factory(DisplayBuffer displayBuffer) {
		switch (displayBuffer.getAnimationType()) {
		case TEXT:
			return new EditTextPanel(displayBuffer);
		case GRAPHIC:
			return new EditGraphicPanel(displayBuffer);
		case REFERENCE:
			return new EditReferencePanel(displayBuffer);
		case MIXED:
			JOptionPane
					.showMessageDialog(
							null,
							"This type of animation cannot be edited or is not supported yet.",
							"Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	public EditPanel(DisplayBuffer displayBuffer) {
		bufferRef = displayBuffer.clone();
		origBuffer = displayBuffer;
		// common components for all types of edit panels
		optionsPanel = new AnimationOptionsPanel();
		rawInputPanel = createRawInputPanel();
		
		setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(5, 5, 5, 5);
		// Left side: Options panel, spanning all rows at 0,0
		c.gridheight = GridBagConstraints.REMAINDER;
		add(optionsPanel, c);
		addMoreComponents();
		add(rawInputPanel, c);
		optionsPanel.addObserver(this);
	}
	
	/**
	 * Overwrite this in children to add components on the right side above the raw edit panel
	 */
	protected abstract void addMoreComponents();
	
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
				setFromDisplayBuffer(bufferRef);
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
		for (int x = 0; x < panel.getCols(); x++) {
			for (int y = 0; y < panel.getRows(); y++) {
				panel.setLed(y, x,
						bufferRef.getValueAt(x + gridCols * position, y));
			}
		}
	}

	public abstract void setFromDisplayBuffer(DisplayBuffer buffer);
	
	public void updateFromRawText() {
		String rawString = bufferRef.getRawString();
		if (!rawInputTextField.getText().equals(rawString)) {
			rawInputTextField.setText(rawString);
		}
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
		// null the buffer references - we rather have a NullpointerException than editing the wrong data! 
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
		AnimatorGui.getInstance().getHomePanel().setVisible(true);
		// TODO this leaks... we need to close and free ourselves if possible!
	}

}
