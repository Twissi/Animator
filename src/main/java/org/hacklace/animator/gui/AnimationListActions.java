package org.hacklace.animator.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class AnimationListActions {
	
	public static class AddAction extends AbstractAction {
		private static final long serialVersionUID = 1859804910358647446L;
		public AddAction() {
			super("+");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			AnimatorGUI.appInstance.getHomePanel().add("New animation");
		}
	}
		
	public static class RemoveAction extends AbstractAction {
		private static final long serialVersionUID = 8727232876038260461L;
		public RemoveAction() {
			super("-");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			AnimatorGUI.appInstance.getHomePanel().removeCurrent();
		}
	}

	public static class MoveUpAction extends AbstractAction {
		private static final long serialVersionUID = 7238977652220630625L;
		public MoveUpAction() {
			super("^");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			AnimatorGUI.appInstance.getHomePanel().moveUp();
		}
	}

	public static class MoveDownAction extends AbstractAction {
		private static final long serialVersionUID = -1322666043099826563L;
		public MoveDownAction() {
			super("V");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			AnimatorGUI.appInstance.getHomePanel().moveDown();
		}
	}

}
