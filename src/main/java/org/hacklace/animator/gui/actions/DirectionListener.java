/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import org.hacklace.animator.enums.Direction;

public class DirectionListener implements ActionListener {

	private final Direction direction;
	private final List<OptionsObserver> optionsObserverList;

	public DirectionListener(Direction direction,
			OptionsObserver optionsObserver) {
		this.direction = direction;
		this.optionsObserverList = new LinkedList<OptionsObserver>();
		addObserver(optionsObserver);
	}

	public void addObserver(OptionsObserver optionsObserver) {
		optionsObserverList.add(optionsObserver);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// currently there is only one observer, the EditPanel
		for (OptionsObserver o : optionsObserverList) {
			o.onDirectionChanged(direction);
		}
	}
}
