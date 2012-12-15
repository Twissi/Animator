package org.hacklace.animator.gui;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.hacklace.animator.HacklaceConfigManager;

public class FileActions {
	
	public static class SaveAsAction extends AbstractAction {
		private static final long serialVersionUID = 3973336765387195380L;
		public SaveAsAction() {
			super("Save as");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Performing SaveAsAction");
		}
	}
	

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
			}
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
}
