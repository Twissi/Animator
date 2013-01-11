package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.gui.EditPanel;

public class DirectionListener implements ActionListener {

	private final EditPanel editPanel;
	private Direction direction;

	public DirectionListener(EditPanel editPanel, Direction direction) {
		this.editPanel = editPanel;
		this.direction = direction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		editPanel.onDirectionChanged(direction);
	}
}
