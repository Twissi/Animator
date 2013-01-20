/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;
import org.hacklace.animator.displaybuffer.MixedDisplayBuffer;
import org.hacklace.animator.displaybuffer.ReferenceDisplayBuffer;
import org.hacklace.animator.displaybuffer.TextDisplayBuffer;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.PredefinedAnimation;
import org.hacklace.animator.gui.AnimatorGui;
import org.hacklace.animator.gui.HomePanel;

public class AnimationListActions {

	public static class AddAction extends AbstractAction {
		private static final long serialVersionUID = 1859804910358647446L;
		private HomePanel homePanel;
		private AnimatorGui animatorGui;

		public AddAction(HomePanel homePanel, AnimatorGui animatorGui) {
			super("Create");
			this.homePanel = homePanel;
			this.animatorGui = animatorGui;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			AnimationType[] options = AnimationType.values();
			AnimationType result = (AnimationType) JOptionPane.showInputDialog(
					animatorGui,
					"Which type of animation would you like to create?",
					"Animation type", JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (result == null)
				return; // cancel

			DisplayBuffer buffer = null;
			switch (result) {
			case GRAPHIC:
				buffer = new GraphicDisplayBuffer();
				break;
			case TEXT:
				buffer = new TextDisplayBuffer();
				break;
			case REFERENCE:
				PredefinedAnimation reference = AskForReferenceHelper.askForReference(animatorGui);
				if (reference == null)
					return; // cancel
				buffer = new ReferenceDisplayBuffer(reference);
				break;
			case MIXED:
				buffer = new MixedDisplayBuffer();
				break;
			}
			homePanel.add(buffer);
			
		}
	}

	public static class RemoveAction extends AbstractAction {
		private static final long serialVersionUID = 8727232876038260461L;
		private HomePanel homePanel;
		private AnimatorGui animatorGui;

		public RemoveAction(HomePanel homePanel, AnimatorGui animatorGui) {
			super("Delete");
			this.homePanel = homePanel;
			this.animatorGui = animatorGui;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!homePanel.isValidSelection())
				return;
			int answer = JOptionPane.showConfirmDialog(
					animatorGui,
					"Do you really want to delete this animation?",
					"Confirm delete", JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				homePanel.removeCurrent();
			}
		}
	}

	public static class MoveUpAction extends AbstractAction {
		private static final long serialVersionUID = 7238977652220630625L;
		private HomePanel homePanel;

		public MoveUpAction(HomePanel homePanel) {
			super("Move up");
			this.homePanel = homePanel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!homePanel.isValidSelection())
				return;
			homePanel.moveUp();
		}
	}

	public static class MoveDownAction extends AbstractAction {
		private static final long serialVersionUID = -1322666043099826563L;
		private HomePanel homePanel;

		public MoveDownAction(HomePanel homePanel) {
			super("Move down");
			this.homePanel = homePanel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!homePanel.isValidSelection())
				return;
			homePanel.moveDown();
		}
	}

	public static class StartEditAction extends AbstractAction {
		private static final long serialVersionUID = 4960734373364680735L;
		private HomePanel homePanel;
		private AnimatorGui animatorGui;

		public StartEditAction(HomePanel homePanel, AnimatorGui animatorGui) {
			super("Edit Animation");
			this.homePanel = homePanel;
			this.animatorGui = animatorGui;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			DisplayBuffer displayBuffer = homePanel.getSelectedBuffer();
			if (displayBuffer == null) {
				return;
			}
			animatorGui.startEditMode(displayBuffer);
		}
	}

	public static class CopyAnimationAction extends AbstractAction {

		private static final long serialVersionUID = -7231020206851674829L;
		private HomePanel homePanel;

		public CopyAnimationAction(HomePanel homePanel) {
			super("Copy");
			this.homePanel = homePanel;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			if (!homePanel.isValidSelection())
				return;

			DisplayBuffer oldDisplayBuffer = homePanel.getSelectedBuffer();
			if (oldDisplayBuffer == null)
				return;

			DisplayBuffer newDisplayBuffer = oldDisplayBuffer.clone();
			homePanel.add(newDisplayBuffer, false);
		}

	}

}
