package org.hacklace.animator.gui;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.enums.AnimationType;

public class FileActions {
	
	public static class OpenAction extends AbstractAction {
		private static final long serialVersionUID = 6197663976216625203L;
		public OpenAction() {
			super("Open");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			FileChooser chooser = new FileChooser();
			File openFile = chooser.inputFile();
			AnimatorGui app = AnimatorGui.getInstance();
			HacklaceConfigManager cm = app.getHacklaceConfigManager();
			HomePanel homePanel = AnimatorGui.getInstance().getHomePanel();
			try {
				cm.clear();
				cm.readFile(openFile);
				homePanel.clear();
			    homePanel.updateList(cm.getList());
			    AnimatorGui.getInstance().setCurrentFile(openFile);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot read from file.", "Error", JOptionPane.ERROR_MESSAGE);
				AnimatorGui.getInstance().getHomePanel().reset();
				// TODO do we need such a function? AnimatorGui.getInstance().getEditAnimationPanel().reset();
			}
			/*
			    List<String> text = Files.readAllLines(Paths.get(openFile.toURI()), StandardCharsets.UTF_8);
			    // ...
			    // TODO do the rest...
			    // ...
			*/
		}
	}
	
	public static class SaveAction extends AbstractAction {
		private static final long serialVersionUID = 3973336765387195380L;
		public SaveAction() {
			super("Save");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Performing SaveAction");
		}
	}
	
	public static class CloseAction extends AbstractAction {
		private static final long serialVersionUID = 7738025108677393058L;
		public CloseAction() {
			super("Close");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
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
			if (index == -1 || index >= cm.getList().size()) return ; // do nothing if nothing valid selected
			DisplayBuffer bufferRef = cm.getList().get(index);
			// Start at first frame
			bufferRef.rewind();
			AnimatorGui.getInstance().getEditAnimationPanel().reset();
			AnimatorGui.getInstance().getEditAnimationPanel().setFromDisplayBuffer(bufferRef, true);
			AnimatorGui.getInstance().getEditAnimationPanel().setMaxPosition(bufferRef.getNumGrids() - 1);
			if (bufferRef.getAnimationType() == AnimationType.TEXT) {
				AnimatorGui.getInstance().setCurrentTabIndex(2);
			} else if (bufferRef.getAnimationType() == AnimationType.GRAPHIC) {
				AnimatorGui.getInstance().setCurrentTabIndex(1);
			} else {
				JOptionPane.showMessageDialog(null, "This type of animation cannot be edited or is not supported yet.", "Error", JOptionPane.ERROR_MESSAGE);
				// required if edit mode was started by clicking tabs:
				AnimatorGui.getInstance().setCurrentTabIndex(0);
			}
		}
	}
}