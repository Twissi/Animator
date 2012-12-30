package org.hacklace.animator.gui;

import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.exporter.BinExporter;
import org.hacklace.animator.exporter.FlashExporter;

public class MenuActions {

	protected static void doNothing(Class<? extends Object> clazz) {
		System.out.println("Performing " + clazz.getSimpleName()
				+ " - doesn't do anything right now");
	}

	private static void loadResource(String fileName) {
		AnimatorGui app = AnimatorGui.getInstance();
		HacklaceConfigManager cm = app.getHacklaceConfigManager();
		HomePanel homePanel = AnimatorGui.getInstance().getHomePanel();
		try {
			InputStream stream = AnimatorGui.class
					.getResourceAsStream(fileName);
			cm.clear();
			cm.readStream(stream);
			homePanel.clear();
			homePanel.updateList(cm.getList(), false);
			AnimatorGui.getInstance().setCurrentFile(null);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"Cannot read from file. Error: " + ex, "Error",
					JOptionPane.ERROR_MESSAGE);
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
			AnimatorGui app = AnimatorGui.getInstance();
			app.stopEditMode();
			app.setCurrentFile(null);
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
			AnimatorGui app = AnimatorGui.getInstance();
			app.stopEditMode();
			app.setCurrentFile(null);
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

	public static class ExportBinAction extends AbstractAction {

		private static final long serialVersionUID = 3972006266609060565L;

		public ExportBinAction() {
			super("Export .bin");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			FileChooser chooser = new FileChooser(".bin");
			File saveAsFile = chooser.outputFile();
			if (saveAsFile == null)
				return; // cancelled
			AnimatorGui app = AnimatorGui.getInstance();
			HacklaceConfigManager cm = app.getHacklaceConfigManager();
			BinExporter binExporter = new BinExporter();
			ByteArrayInputStream stream = null;
			try {
				stream = new ByteArrayInputStream(cm.getRawString().getBytes(
						HacklaceConfigManager.HACKLACE_CHARSET));
				binExporter.write(stream, saveAsFile);
			} // Java 7: (IOException | UnsupportedCommOperationException |
				// PortInUseException ex)
			catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Error writing file: "
						+ ex, "Error", JOptionPane.ERROR_MESSAGE);
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null, "Error closing stream: "
							+ ex, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public static class FlashAction extends AbstractAction {

		private static final long serialVersionUID = 3492735544537440621L;

		public FlashAction() {
			super("Flash hacklace");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			AnimatorGui app = AnimatorGui.getInstance();
			HacklaceConfigManager cm = app.getHacklaceConfigManager();
			int bytesUsed = cm.getBytesUsed();
			int maxBytes = IniConf.getInstance().maxBytes();
			if (bytesUsed > maxBytes) {
				JOptionPane.showMessageDialog(null, "Error flashing hacklace: Animation list too big ("
						+ bytesUsed + "/" + maxBytes + " Bytes)",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			FlashExporter flashExporter = new FlashExporter();
			ByteArrayInputStream stream;
			try {
				stream = new ByteArrayInputStream(cm.getRawString().getBytes(
						HacklaceConfigManager.HACKLACE_CHARSET));
				flashExporter.write(stream);
				JOptionPane.showMessageDialog(null,
						"Hacklace successfully flashed.", "Flashed",
						JOptionPane.INFORMATION_MESSAGE);
			} // Java 7: (IOException | UnsupportedCommOperationException |
				// PortInUseException ex)
			catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Error flashing hacklace: "
						+ ex, "Error", JOptionPane.ERROR_MESSAGE);
			} catch (UnsupportedCommOperationException ex) {
				JOptionPane.showMessageDialog(null, "Error flashing hacklace: "
						+ ex, "Error", JOptionPane.ERROR_MESSAGE);
			} catch (PortInUseException ex) {
				JOptionPane.showMessageDialog(null, "Error flashing hacklace: "
						+ ex, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static class SaveAsAction extends AbstractAction {
		private static final long serialVersionUID = 3973336765387195380L;

		public SaveAsAction() {
			super("Save *.hack file as");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			FileChooser chooser = new FileChooser(".hack");
			File saveAsFile = chooser.outputFile();
			if (saveAsFile == null)
				return; // cancelled
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
			FileChooser chooser = new FileChooser(".hack");
			File openFile = chooser.inputFile();
			if (openFile == null)
				return; // cancelled
			AnimatorGui app = AnimatorGui.getInstance();
			HacklaceConfigManager cm = app.getHacklaceConfigManager();
			HomePanel homePanel = AnimatorGui.getInstance().getHomePanel();
			try {
				cm.clear();
				cm.readFile(openFile);
				homePanel.clear();
				homePanel.updateList(cm.getList(), false);
				app.setCurrentFile(openFile);
				app.stopEditMode();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot read from file.",
						"Error", JOptionPane.ERROR_MESSAGE);
				app.getHomePanel().reset();
				app.stopEditMode();
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
					JOptionPane.showMessageDialog(null,
							"Cannot write to file.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				new SaveAsAction().actionPerformed(e);
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
