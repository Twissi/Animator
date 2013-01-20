/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.ReferenceDisplayBuffer;
import org.hacklace.animator.gui.actions.ChooseReferenceActionListener;

public class EditReferencePanel extends EditPanel {

	private static final long serialVersionUID = 1648909855223726429L;

	public EditReferencePanel(DisplayBuffer displayBuffer, HomePanel homePanel, AnimatorGui animatorGui, HacklaceConfigManager configManager) {
		super(displayBuffer, homePanel, animatorGui, configManager);
		ledPanel.setEnabled(false);
	}

	@Override
	protected void addMoreComponents(JPanel panel) {
		JButton button = new JButton("Choose reference");
		panel.add(button);
		button.addActionListener(new ChooseReferenceActionListener(this, animatorGui));
	}

	@Override
	protected int getMaximumGrid() {
		int columns = getBuffer().getNumColumns();
		int grids = ((int) (columns / GRID_COLS)) - NUM_GRIDS_TO_SHOW;
		if (grids < 0)
			grids = 0;
		return grids;
	}

	@Override
	public ReferenceDisplayBuffer getBuffer() {
		return (ReferenceDisplayBuffer) super.getBuffer();
	}

}
