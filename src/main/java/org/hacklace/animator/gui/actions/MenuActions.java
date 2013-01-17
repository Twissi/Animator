package org.hacklace.animator.gui.actions;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.exporter.BinExporter;
import org.hacklace.animator.exporter.FlashExporter;
import org.hacklace.animator.gui.AnimatorGui;
import org.hacklace.animator.gui.FileChooser;
import org.hacklace.animator.gui.HomePanel;

public class MenuActions {

	private static String confirmationText = "Any changes to the current animation list will be lost. Do you really want to continue?";
	private static String confirmationTitle = "Really?";

	protected static void doNothing(Class<? extends Object> clazz) {
		System.out.println("Performing " + clazz.getSimpleName()
				+ " - doesn't do anything right now");
	}

	protected static boolean confirm(String text, String title) {
		int result = JOptionPane.showConfirmDialog(null, text, title,
				JOptionPane.OK_CANCEL_OPTION);
		return (result == JOptionPane.OK_OPTION);
	}

	protected static boolean confirm() {
		return confirm(confirmationText, confirmationTitle);
	}

	private static void loadHacklaceConfigFileAsResource(String fileName,
			ErrorContainer errorContainer) {
		AnimatorGui app = AnimatorGui.getInstance();
		HacklaceConfigManager cm = app.getHacklaceConfigManager();
		HomePanel homePanel = AnimatorGui.getInstance().getHomePanel();
		InputStream stream = null;
		try {
			stream = AnimatorGui.class.getResourceAsStream(fileName);
			cm.clear();
			cm.readStream(stream, errorContainer);
			homePanel.clear();
			homePanel.updateList(cm.getList(), false);
			AnimatorGui.getInstance().setCurrentFile(null);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"Cannot read from file. Error: " + ex, "Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			AnimatorGui.getInstance().getHomePanel().reset();
		} finally {
			if (stream != null)
				try {
					stream.close();
				} catch (IOException ex) {
					// do nothing
				}
		}
	}

	private abstract static class AbstractLoadExample extends AbstractAction {

		private static final long serialVersionUID = 3429284491017128252L;

		public AbstractLoadExample(String s) {
			super(s);
		}

		@Override
		public abstract void actionPerformed(ActionEvent arg0);

		protected void load(String resource) {
			if (!confirm())
				return;
			ErrorContainer errorContainer = new ErrorContainer();
			loadHacklaceConfigFileAsResource(resource, errorContainer);
			AnimatorGui app = AnimatorGui.getInstance();
			app.endEditMode();
			app.setCurrentFile(null);
			// TODO display errors from errorContainer
		}

	}

	public static class LoadDefaultAction extends AbstractLoadExample {

		private static final long serialVersionUID = -8252301301328863615L;

		public LoadDefaultAction() {
			super("Load default configuration");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			load("/Default_Configuration.hack");
		}

	}

	public static class LoadExampleAction extends AbstractLoadExample {

		private static final long serialVersionUID = 5758517032413260605L;

		public LoadExampleAction() {
			super("Load example configuration");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			load("/example.hack");
		}

	}

	public static class ExportGifAction extends AbstractAction {

		private static final long serialVersionUID = 3972006266609060565L;

		public ExportGifAction() {
			super("Export *.gif");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			doNothing(this.getClass());

		}

	}

	public static class ExportBinAction extends AbstractAction {

		private static final long serialVersionUID = 3972006266609060565L;

		public ExportBinAction() {
			super("Export *.bin");
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
			try {
				binExporter.write(cm.getRawString(), saveAsFile);
			} // Java 7: (IOException | UnsupportedCommOperationException |
				// PortInUseException ex)
			catch (IOException ex) {
				JOptionPane.showMessageDialog(null,
						"Error writing file: " + ex, "Error",
						JOptionPane.ERROR_MESSAGE);
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
			AnimatorGui animatorGui = AnimatorGui.getInstance();
			HacklaceConfigManager cm = animatorGui.getHacklaceConfigManager();
			int bytesUsed = cm.getNumBytes();
			int maxBytes = AnimatorGui.getIniConf().maxBytes();
			if (bytesUsed > maxBytes) {
				JOptionPane.showMessageDialog( //
						animatorGui, // parent
						"Error flashing hacklace: Animation list too big ("
								+ bytesUsed + "/" + maxBytes + " Bytes)", // text
						"Error", // title
						JOptionPane.ERROR_MESSAGE // type
						);
				return;
			}

			String path = "/hacklace-flashen.jpg";
			URL url = this.getClass().getResource(path);
			Icon icon = new ImageIcon(url);

			JOptionPane
					.showMessageDialog(
							animatorGui, // parent
							"", // text
							"Connect the hacklace directly to an USB port (not a hub) and turn it on", // title
							JOptionPane.INFORMATION_MESSAGE, // type
							icon // picture
					);

			// Let the user select the serial port
			FlashExporter flashExporter = new FlashExporter();
			ArrayList<String> ports = flashExporter.listDeviceNames();

			String problemText = "Make sure you have the necessary authorizations. \n"
					+ "Linux: group dialout. BSD: group dialer. Log in again after changing groups. \n"
					+ "Alternatively use su(do). \n"
					+ "Connect directly to USB ports, not to USB hubs. \n";

			int numPorts = ports.size();
			if (numPorts == 0) {
				JOptionPane.showMessageDialog( //
						animatorGui, // parent
						problemText, // text
						"No serial port found", // title
						JOptionPane.ERROR_MESSAGE // type
						);
				return;
			}

			String defaultPortString = "";
			try {
				CommPortIdentifier defaultPort = flashExporter
						.getPortIdentifier(AnimatorGui.getIniConf().device());
				if (defaultPort != null)
					defaultPortString = defaultPort.toString();
			} catch (Exception ex) {
				// the default port from the ini file does not exist on this
				// system. -> ignore
			}

			String selectionString = "Select the serial port where the Hacklace is connected and turn it on.\n"
					+ "If you are unsure which one to select, use dmesg directly after connecting (Linux/BSD). \n"
					+ "If the right port is not in the list: \n" + problemText;

			String port = (String) JOptionPane.showInputDialog(animatorGui, // parent
					selectionString, // text
					"Select port", // title
					JOptionPane.QUESTION_MESSAGE, // type
					null, // icon
					ports.toArray(), // choices
					defaultPortString // default choice
					);
			if (port == null)
				return; // cancelled
			flashExporter.setDeviceName(port);

			try {
				flashExporter.write(cm.getRawString());
				JOptionPane.showMessageDialog(null, // parent
						"Hacklace has been flashed.", // message
						"Flashed", // title
						JOptionPane.INFORMATION_MESSAGE); // type
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
			ErrorContainer errorContainer = new ErrorContainer();
			try {
				cm.clear();
				cm.readFile(openFile, errorContainer);
				homePanel.clear();
				homePanel.updateList(cm.getList(), false);
				app.setCurrentFile(openFile);
				app.endEditMode();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot read from file.",
						"Error", JOptionPane.ERROR_MESSAGE);
				app.getHomePanel().reset();
				app.endEditMode();
			}
			JOptionPane.showMessageDialog(null, "Problems in file"
					+ errorContainer.toString(), "Error",
					JOptionPane.ERROR_MESSAGE);
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
			if (!confirm("Do you really want to quit?", "Quit?"))
				return;
			AnimatorGui.getInstance().endEditMode();
			AnimatorGui.getInstance().dispose();
		}
	}

	public static class HelpAction extends AbstractAction {

		private static final long serialVersionUID = 6474820875394474686L;

		public HelpAction() {
			super("Help");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			AnimatorGui.getInstance().showHelp();
		}
	}

	public static class NewAction extends AbstractAction {

		private static final long serialVersionUID = -1969161857609413789L;

		public NewAction() {
			super("New");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!confirm())
				return;
			AnimatorGui app = AnimatorGui.getInstance();
			app.getHomePanel().clear();
			app.getHacklaceConfigManager().clear();
			app.endEditMode();
			app.setCurrentFile(null);
		}
	}

}
