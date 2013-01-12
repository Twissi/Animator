package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.gui.OptionsObserver;

public class DirectionListener implements ActionListener {

	private final OptionsObserver optionsObserver;
	private Direction direction;

	public DirectionListener(OptionsObserver optionsObserver, Direction direction) {
		this.optionsObserver = optionsObserver;
		this.direction = direction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		optionsObserver.onDirectionChanged(direction);
	}
}
