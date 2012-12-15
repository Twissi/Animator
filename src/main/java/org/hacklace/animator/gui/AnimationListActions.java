package org.hacklace.animator.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.enums.AnimationType;

public class AnimationListActions {

	public static class AddAction extends AbstractAction {
		private static final long serialVersionUID = 1859804910358647446L;

		public AddAction() {
			super("+");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object[] options = { "Graphic", "Text" };
			String result = (String) JOptionPane.showInputDialog(AnimatorGui.getInstance(),
					"Which type of animation would you like to create?",
					"Animation type", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			HacklaceConfigManager cm = AnimatorGui.getInstance().getHacklaceConfigManager();
			if (result == null) return; // cancel
			else if (result.equals((String)options[0])) {
				cm.addGraphicDisplayBuffer();
				AnimatorGui.getInstance().getHomePanel().add(cm.getDisplayBuffer(cm.getList().size() - 1));
			} else if (result.equals((String)options[1])) {
				cm.addTextDisplayBuffer();
				AnimatorGui.getInstance().getHomePanel().add(cm.getDisplayBuffer(cm.getList().size() - 1));
			}
		}
	}

	public static class RemoveAction extends AbstractAction {
		private static final long serialVersionUID = 8727232876038260461L;

		public RemoveAction() {
			super("-");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int n = JOptionPane.showConfirmDialog(
				    AnimatorGui.getInstance(),
				    "Do you really want to delete this animation?",
				    "Confirm delete",
				    JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				AnimatorGui.getInstance().getHomePanel().removeCurrent();
			}
		}
	}

	public static class MoveUpAction extends AbstractAction {
		private static final long serialVersionUID = 7238977652220630625L;

		public MoveUpAction() {
			super("^");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = AnimatorGui.getInstance().getHomePanel().moveUp();
			if (index != -1) {
				AnimatorGui.getInstance().getHacklaceConfigManager().moveUp(index);
			}
		}
	}

	public static class MoveDownAction extends AbstractAction {
		private static final long serialVersionUID = -1322666043099826563L;

		public MoveDownAction() {
			super("V");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = AnimatorGui.getInstance().getHomePanel().moveDown();
			if (index != -1) {
				AnimatorGui.getInstance().getHacklaceConfigManager().moveDown(index);
			}
		}
	}

	public static class StartEditAction extends AbstractAction {
		private static final long serialVersionUID = 4960734373364680735L;
		public StartEditAction() {
			super("Edit");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			int index = AnimatorGui.getInstance().getHomePanel().getSelectedIndex();
	        HacklaceConfigManager cm = AnimatorGui.getInstance().getHacklaceConfigManager();
			if (index == -1 || index >= cm.getList().size()) {
				// Stay at first tab and do nothing if no valid selection
				AnimatorGui.getInstance().setCurrentTabIndex(0);
				return ;
			}
			DisplayBuffer bufferRef = cm.getList().get(index);
			// Start at first frame
			bufferRef.rewind();
			EditAnimationPanel panel = AnimatorGui.getInstance().getEditAnimationPanel();
			panel.reset();
			panel.setFromDisplayBuffer(bufferRef, true);
			panel.setMaxPosition(bufferRef.getNumGrids() - 1);
			if (bufferRef.getAnimationType() == AnimationType.TEXT || bufferRef.getAnimationType() == AnimationType.GRAPHIC) {
				AnimatorGui.getInstance().setCurrentTabIndex(1);
			} else {
				JOptionPane.showMessageDialog(null, "This type of animation cannot be edited or is not supported yet.", "Error", JOptionPane.ERROR_MESSAGE);
				// required if edit mode was started by clicking tabs:
				AnimatorGui.getInstance().setCurrentTabIndex(0);
			}
		}
	}

}
