/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui.actions;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hacklace.animator.enums.Delay;

public class DelayChangeListener implements ChangeListener {

	private final JSlider delaySlider;
	private final List<OptionsObserver> optionsObserverList;

	public DelayChangeListener(JSlider delaySlider,
			OptionsObserver optionsObserver) {
		this.delaySlider = delaySlider;
		this.optionsObserverList = new LinkedList<OptionsObserver>();
		addObserver(optionsObserver);
	}

	public void addObserver(OptionsObserver optionsObserver) {
		optionsObserverList.add(optionsObserver);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		int intDelay = delaySlider.getValue();
		// currently there is only one observer, the EditPanel
		for (OptionsObserver o : optionsObserverList) {
			o.onDelayChanged(Delay.fromInt(intDelay));
		}

	}
}
