package org.hacklace.animator.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
			AnimatorGUI app = AnimatorGUI.appInstance;
			FileChooser chooser = new FileChooser();
			File openFile = chooser.inputFile();
			try {
			    List<String> text = Files.readAllLines(Paths.get(openFile.toURI()), StandardCharsets.UTF_8);
			    AnimatorGUI.appInstance.getHomePanel().updateList(text);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error", "Cannot read from file.", JOptionPane.ERROR_MESSAGE);
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