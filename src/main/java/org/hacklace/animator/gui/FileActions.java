package org.hacklace.animator.gui;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.hacklace.animator.HacklaceConfigManager;

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
			try {
				cm.readFile(openFile);
			    AnimatorGui.getInstance().getHomePanel().updateList(cm.getList());
			    AnimatorGui.getInstance().setCurrentFile(openFile);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot read from file.", "Error", JOptionPane.ERROR_MESSAGE);
				AnimatorGui.getInstance().getHomePanel().reset();
				AnimatorGui.getInstance().getEditAnimationPanel().reset();
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
			int tabIndex = AnimatorGui.getInstance().getCurrentTabIndex();
			if (tabIndex != 1) ; // TODO AnimatorGUI.getInstance().setTabIndex(1);
			int index = AnimatorGui.getInstance().getHomePanel().getSelectedIndex();
			HacklaceConfigManager cm = AnimatorGui.getInstance().getHacklaceConfigManager();
			AnimatorGui.getInstance().getEditAnimationPanel().setFromDisplayBuffer(cm.getDisplayBuffer(index));
		}
	}
}