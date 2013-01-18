package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;

public class SaveAnimationAction extends AbstractAction {

	private static final long serialVersionUID = -5813301123661228603L;
	private final List<SaveObserver> saveObserverList;

	public SaveAnimationAction(
			SaveObserver saveObserver) {
		super("Save");
		this.saveObserverList = new LinkedList<SaveObserver>();
		addObserver(saveObserver);
	}
	
	public void addObserver(SaveObserver saveObserver) {
		saveObserverList.add(saveObserver);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (SaveObserver o : saveObserverList) {
			o.onSaveAnimation();
		}
	}
}