package org.hacklace.animator.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.hacklace.animator.IniConf;
import org.hacklace.animator.gui.actions.ToggleLedActionListener;

public class LedPanel extends JPanel implements LedObserver {

	private static final long serialVersionUID = 3966890869284361505L;

	private final int gridRows;
	private final int gridCols;

	private Led[][] buttons;
	private List<JLabel> frameLabels = new ArrayList<JLabel>();
	private GridBagLayout layout;
	private boolean isSpaced = false;
	private int offset = 0;
	
	private List<LedObserver> observerList;

	public LedPanel(int rows, int columns) {
		this.gridRows = rows;
		this.gridCols = columns;

		buttons = new Led[gridCols][gridRows];
		observerList = new ArrayList<LedObserver>();

		initComponents();
		setVisible(true);
	}

	public LedPanel() {
		this(IniConf.getInstance().rows(), IniConf.getInstance().columns());
	}

	public void setLedFromBuffer(int column, int row, boolean val) {
		
		buttons[column][row].setFromBuffer(val);

		// for (LedObserver o: observerList) {
		// o.onLedChange(row, column, val);
		// }
	}

	private void initComponents() {
		layout = new GridBagLayout();
		setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		// add leds
		for (c.gridy = 0; c.gridy < gridRows; c.gridy++) {
			for (c.gridx = 0; c.gridx < gridCols; c.gridx++) {
				Led b = new Led(c.gridy, c.gridx, this);
				b.addActionListener(new ToggleLedActionListener(b));
				buttons[c.gridx][c.gridy] = b;
				add(b, c);
			}
		}
		// add labels for frame number
		c.gridwidth = 5;
		c.anchor = GridBagConstraints.CENTER;
		for (c.gridx = 0; c.gridx < gridCols; c.gridx+=5) {
			JLabel frameLabel = new JLabel();
			add(frameLabel, c);
			frameLabels.add(frameLabel);
		}
		updateLabels();
	}
	
	private void updateLabels() {
		for (int i = 0; i < frameLabels.size(); i++) {
			frameLabels.get(i).setText(Integer.toString(i + offset));
		}
	}
	
//	public void clear() {
//		for (int x = 0; x < gridCols; x++) {
//			for (int y = 0; y < gridRows; y++) {
//				setLed(y, x, false);
//			}
//		}
//	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (int x = 0; x < gridCols; x++) {
			for (int y = 0; y < gridRows; y++) {
				buttons[x][y].setEnabled(enabled);
			}
		}
	}
	
	private void updateGridSpacing() {
		GridBagConstraints c = new GridBagConstraints();
		if (isSpaced) {
			c.insets = new Insets(0, 0, 0, 5);
		}
		for (int i = 0; i < gridCols; i++) {
			if (i % 5 == 4) {
				layout.setConstraints(buttons[i][0], c);
			}
		}
		revalidate();
		repaint();
	}

	public void addObserver(LedObserver o) {
		observerList.add(o);
	}
	
	/**
	 * This bubbles the change events from leds to the ledPanel's registered observers
	 */
	public void onLedChange(int row, int column, boolean newValue) {
		for (LedObserver o: observerList) {
			o.onLedChange(row, column, newValue);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(gridCols * 15, gridRows * 15);
	}
	
	public int getRows() {
		return gridRows;
	}
	
	public int getCols() {
		return gridCols;
	}
	
	public void toggleSpacing() {
		isSpaced = !isSpaced;
		updateGridSpacing();
	}

	public void setSpacing(boolean value) {
		isSpaced = value;
		updateGridSpacing();
	}
	
	public void setOffset(int value) {
		offset = value;
		updateLabels();
	}
	
	public void showLabels(boolean value) {
		for (JLabel label: frameLabels) {
			label.setVisible(value);
		}
	}
	
//	public static void main(String[] args) throws InterruptedException {
//		JFrame f = new JFrame("Test");
//		final LedPanel p = new LedPanel(7, 50);
//		f.setSize(500, 700);
//		f.getContentPane().setLayout(new GridLayout());
//		f.getContentPane().add(p);
//		JButton button = new JButton("Toggle spacing");
//		button.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				p.toggleSpacing();
//			}}
//		);
//		f.getContentPane().add(button);
//		
//		f.pack();
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		f.setVisible(true);
//		for (int i = FontUtil.LOWEST_INDEX; i < FontUtil.HIGHEST_INDEX + 1; i++) {
//			p.grid.setDataFromBytes(FontUtil.getFiveBytesForIndex(i));
//			for (int row = 0; row < p.gridRows; row++) {
//				for (int column = 0; column < p.gridCols; column++) {
//					p.setLed(row, column, p.grid.getColumnRow(column,row));
//				}
//
//			}
//			Thread.sleep(1000);
//
//		}
//
//	}
}