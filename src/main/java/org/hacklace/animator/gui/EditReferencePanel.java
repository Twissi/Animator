package org.hacklace.animator.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JSlider;

import org.hacklace.animator.IniConf;
import org.hacklace.animator.displaybuffer.DisplayBuffer;

public class EditReferencePanel extends EditPanel {

	private static final long serialVersionUID = 1648909855223726429L;
	
	public EditReferencePanel(DisplayBuffer displayBuffer) {
		super(displayBuffer);
	}

	@Override
	protected void addMoreComponents(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		ledPanel = new LedPanel(IniConf.getInstance().rows(), IniConf.getInstance().columns() * 5);
		ledPanel.setEnabled(false);
		panel.add(ledPanel, c);
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		JSlider positionSlider = createPositionSlider();
		panel.add(positionSlider, c);
	}

	@Override
	public void setFromDisplayBuffer(DisplayBuffer buffer) {
		super.setFromDisplayBuffer(buffer);
		copyBufferToPanel(currentPosition, ledPanel);
	}
}
