package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.PredefinedAnimation;
import org.hacklace.animator.gui.AnimatorGui;
import org.hacklace.animator.gui.HomePanel;

public class AnimationListActions {

	public static int askForReference(char selectedLetter) {
		PredefinedAnimation selected = null;
		for (PredefinedAnimation animation: PredefinedAnimation.values()) {
			if (animation.getIndex() == selectedLetter) {
				selected = animation;
			}
		}
		PredefinedAnimation result = (PredefinedAnimation) JOptionPane
				.showInputDialog(
						AnimatorGui.getInstance(),
						"Please select the number of the referenced animation. A is the first, B the second, etc.",
						"Animation number",
						JOptionPane.QUESTION_MESSAGE, null, PredefinedAnimation.values(),
						selected);
		if (result == null)
			return -1; // cancel
		return result.getIndex();
	}
	
	public static class AddAction extends AbstractAction {
		private static final long serialVersionUID = 1859804910358647446L;
		private HomePanel homePanel;
		private HacklaceConfigManager configManager;

		public AddAction(HomePanel homePanel,
				HacklaceConfigManager hacklaceConfigManager) {
			super("Create");
			this.homePanel = homePanel;
			this.configManager = hacklaceConfigManager;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			AnimationType[] options = AnimationType.values();
			AnimationType result = (AnimationType) JOptionPane.showInputDialog(
					AnimatorGui.getInstance(),
					"Which type of animation would you like to create?",
					"Animation type", JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (result == null)
				return; // cancel

			DisplayBuffer buffer = null;
			switch (result) {
			case GRAPHIC:
				buffer = configManager.addGraphicDisplayBuffer();
				break;
			case TEXT:
				buffer = configManager.addTextDisplayBuffer();
				break;
			case REFERENCE:
				int letter = askForReference('A');
				if (letter == -1) return; // cancel
				buffer = configManager.addReferenceDisplayBuffer((char)letter);
				break;
			case MIXED:
				buffer = configManager.addMixedDisplayBuffer();
				break;
			}
			homePanel.add(buffer);
			homePanel.updateList(configManager.getList(), true);
		}
	}

	public static class RemoveAction extends AbstractAction {
		private static final long serialVersionUID = 8727232876038260461L;
		private HomePanel homePanel;
		private HacklaceConfigManager configManager;

		public RemoveAction(HomePanel homePanel,
				HacklaceConfigManager configManager) {
			super("Delete");
			this.homePanel = homePanel;
			this.configManager = configManager;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int answer = JOptionPane.showConfirmDialog(
					AnimatorGui.getInstance(),
					"Do you really want to delete this animation?",
					"Confirm delete", JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				int index = homePanel.removeCurrent();
				if (index > -1) {
					configManager.getList().remove(index);
					homePanel.updateList(configManager.getList(), true);
				}
			}
		}
	}

	public static class MoveUpAction extends AbstractAction {
		private static final long serialVersionUID = 7238977652220630625L;
		private HomePanel homePanel;
		private HacklaceConfigManager configManager;

		public MoveUpAction(HomePanel homePanel,
				HacklaceConfigManager configManager) {
			super("Move up");
			this.homePanel = homePanel;
			this.configManager = configManager;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = homePanel.moveUp();
			if (index != -1) {
				configManager.moveUp(index);
			}
			homePanel.updateList(configManager.getList(), true);
		}
	}

	public static class MoveDownAction extends AbstractAction {
		private static final long serialVersionUID = -1322666043099826563L;
		private HomePanel homePanel;
		private HacklaceConfigManager configManager;

		public MoveDownAction(HomePanel homePanel,
				HacklaceConfigManager configManager) {
			super("Move down");
			this.homePanel = homePanel;
			this.configManager = configManager;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = homePanel.moveDown();
			if (index != -1) {
				configManager.moveDown(index);
			}
			homePanel.updateList(configManager.getList(), true);
		}
	}

	public static class StartEditAction extends AbstractAction {
		private static final long serialVersionUID = 4960734373364680735L;
		private HomePanel homePanel;
		private HacklaceConfigManager configManager;
		private AnimatorGui animatorGui;

		public StartEditAction(HomePanel homePanel,
				HacklaceConfigManager configManager, AnimatorGui animatorGui) {
			super("Edit Animation");
			this.homePanel = homePanel;
			this.configManager = configManager;
			this.animatorGui = animatorGui;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = homePanel.getSelectedIndex();
			if (index == -1 || index >= configManager.getList().size()) {
				// do nothing if no valid selection
				return;
			}
			DisplayBuffer displayBuffer = configManager.getList().get(index);
			animatorGui.startEditMode(displayBuffer);
		}
	}

}
