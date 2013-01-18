package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import org.hacklace.animator.enums.StepWidth;

public class StepWidthListener implements ActionListener {

	private final StepWidth stepWidth;
	private final List<StepWidthObserver> stepWidthObserverList;

	public StepWidthListener(StepWidth stepWidth,
			StepWidthObserver stepWidthObserver) {
		this.stepWidth = stepWidth;
		this.stepWidthObserverList = new LinkedList<StepWidthObserver>();
		addObserver(stepWidthObserver);
	}

	public void addObserver(StepWidthObserver optionsObserver) {
		stepWidthObserverList.add(optionsObserver);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// currently there is only one observer, the EditPanel
		for (StepWidthObserver o : stepWidthObserverList) {
			o.onStepChanged(stepWidth);
		}
	}
}
