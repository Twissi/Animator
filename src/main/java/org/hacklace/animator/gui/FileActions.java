package org.hacklace.animator.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.hacklace.animator.HacklaceConfigManager;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
			AnimatorGUI app = AnimatorGUI.appInstance;
			HacklaceConfigManager cm = app.getHacklaceConfigManager(); 
			try {
				cm.readFile(openFile);
			    AnimatorGUI.appInstance.getHomePanel().updateList(cm.getList());
			    AnimatorGUI.appInstance.setCurrentFile(openFile);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot read from file.", "Error", JOptionPane.ERROR_MESSAGE);
				AnimatorGUI.appInstance.getHomePanel().reset();
				AnimatorGUI.appInstance.getEditAnimationPanel().reset();
			}
			/*
			    List<String> text = Files.readAllLines(Paths.get(openFile.toURI()), StandardCharsets.UTF_8);
			    // ...
			    // @TODO do the rest...
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
			int tabIndex = AnimatorGUI.appInstance.getCurrentTabIndex();
			if (tabIndex != 1) ; // @TODO AnimatorGUI.appInstance.setTabIndex(1);
			int index = AnimatorGUI.appInstance.getHomePanel().getSelectedIndex();
			HacklaceConfigManager cm = AnimatorGUI.appInstance.getHacklaceConfigManager();
			AnimatorGUI.appInstance.getEditAnimationPanel().setFromDisplayBuffer(cm.getDisplayBuffer(index));
		}
	}
}