package org.hacklace.animator.gui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.hacklace.animator.HacklaceConfigManager;

public class MenuActions {
	
	protected static void doNothing(Class<? extends Object> clazz) {
		System.out.println("Performing "+clazz.getSimpleName()+" - doesn't do anything right now");
	}
	
	private static void loadResource(String fileName) {
		AnimatorGui app = AnimatorGui.getInstance();
		HacklaceConfigManager cm = app.getHacklaceConfigManager();
		HomePanel homePanel = AnimatorGui.getInstance().getHomePanel();
		try {
			InputStream stream = AnimatorGui.getInstance().getClass().getResourceAsStream(fileName);
			cm.clear();
			cm.readStream(stream);
			homePanel.clear();
			homePanel.updateList(cm.getList(), false);
			AnimatorGui.getInstance().setCurrentFile(null);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Cannot read from file. Error: " + ex,
					"Error", JOptionPane.ERROR_MESSAGE);
			AnimatorGui.getInstance().getHomePanel().reset();
		}
	}

	public static class LoadDefaultAction extends AbstractAction {

		private static final long serialVersionUID = -8252301301328863615L;
		public LoadDefaultAction() {
			super("Load default configuration");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			loadResource("/Default_Konfiguration.txt");
		}

	}

	public static class LoadExampleAction extends AbstractAction {

		private static final long serialVersionUID = 5758517032413260605L;
		public LoadExampleAction() {
			super("Load example configuration");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			loadResource("/example.cfg");
		}

	}

	public static class ExportGifAction extends AbstractAction {

		private static final long serialVersionUID = 3972006266609060565L;
		public ExportGifAction() {
			super("Export gif");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			doNothing(this.getClass());

		}

	}

	public static class FlashAction extends AbstractAction {

		private static final long serialVersionUID = 3492735544537440621L;
		public FlashAction() {
			super("Flash hacklace");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			doNothing(this.getClass());
		}
	}

	public static class SaveAsAction extends AbstractAction {
		private static final long serialVersionUID = 3973336765387195380L;
		public SaveAsAction() {
			super("Save *.hack file as");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			FileChooser chooser = new FileChooser();
			File saveAsFile = chooser.outputFile();
			AnimatorGui app = AnimatorGui.getInstance();
			HacklaceConfigManager cm = app.getHacklaceConfigManager();
			try {
				cm.writeFile(saveAsFile);
				AnimatorGui.getInstance().setCurrentFile(saveAsFile);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Cannot write to file.",
						"Error", JOptionPane.ERROR_MESSAGE);
				AnimatorGui.getInstance().getHomePanel().reset();
			}
		}
	}

	public static class OpenAction extends AbstractAction {
		private static final long serialVersionUID = 6197663976216625203L;
		public OpenAction() {
			super("Open *.hack file");
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
				homePanel.updateList(cm.getList(), false);
				AnimatorGui.getInstance().setCurrentFile(openFile);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot read from file.",
						"Error", JOptionPane.ERROR_MESSAGE);
				AnimatorGui.getInstance().getHomePanel().reset();
			}
		}
	}

	public static class SaveAction extends AbstractAction {
		private static final long serialVersionUID = 3973336765387195380L;
		public SaveAction() {
			super("Save *.hack file");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			AnimatorGui app = AnimatorGui.getInstance();
			HacklaceConfigManager cm = app.getHacklaceConfigManager();
			if (app.getCurrentFile() != null) {
				try {
					cm.writeFile(app.getCurrentFile());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Cannot write to file.",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
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

	public static class HelpAction extends AbstractAction {

		private static final long serialVersionUID = 6474820875394474686L;

		public HelpAction() {
			super("Help");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			doNothing(this.getClass());
			// try {
			// HelpSet hs = new HelpSet(null, hsURL);
			// } catch (Exception ee) {
			// // Say what the exception really is
			// System.out.println( "HelpSet " + ee.getMessage());
			// System.out.println("HelpSet "+ helpHS +" not found")
			// return;
			// }
		}
	}

}
