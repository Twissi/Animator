/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.help.JHelp;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.gui.actions.MenuActions;

public class AnimatorGui extends JFrame {

	private static final long serialVersionUID = 2757544085601062109L;

	private File currentFile;

	private HomePanel homePanel;
	private EditPanel editPanel = null;
	private Container contentPane;
	private Color ledColor = Color.green;

	private final HacklaceConfigManager hacklaceConfigManager;

	private static IniConf iniConf = new IniConf();

	private String title = "The chosen Hacklace Animator Tool";
	private String HACKLACE_HELP_URL = "https://github.com/Hacklace/Animator/wiki/Help";

	public AnimatorGui() {
		hacklaceConfigManager = new HacklaceConfigManager();
		initComponents();
		super.setVisible(true);
	}

	public AnimatorGui(final String fileName) {
		this();
		final ErrorContainer errorContainer = new ErrorContainer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				loadFile(fileName, errorContainer);
			}
		});
	}

	/**
	 * Show the help.
	 * This will fall back to using JavaHelp if no default browser can be started
	 */
	public void showHelp() {

		if (iniConf.helpOnline() && java.awt.Desktop.isDesktopSupported()) {
			java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
			if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
				try {
					java.net.URI uri = new java.net.URI(HACKLACE_HELP_URL);
					desktop.browse(uri);
					return;
				} catch (Exception e) {
					// TODO what do do in case of exception?
				}
			}
		}

		URL url = HelpSet.findHelpSet(null, "help/jhelpset.hs");
		JHelp helpViewer = null;
		try {
			helpViewer = new JHelp(new HelpSet(null, url));
			// helpViewer.setCurrentID("...");
			JFrame frame = new JFrame();
			frame.setTitle("Hacklace Animator Help");
			frame.setSize(800, 600);
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
			frame.getContentPane().add(helpViewer);
		} catch (HelpSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public File getCurrentFile() {
		return currentFile;
	}

	public void setCurrentFile(File file) {
		currentFile = file;
		if (currentFile != null && currentFile.getName() != "") {
			setTitle(currentFile.getName() + " - " + title);
		} else {
			setTitle(title);
		}

	}

	public HomePanel getHomePanel() {
		return homePanel;
	}

	private void initComponents() {
		// Note: need to set layout or the second panel will overwrite the first
		setLayout(new FlowLayout());
		JMenuBar menuBar = new JMenuBar();
		// file menu
		JMenu menuFile = new JMenu("File");
		menuFile.add(new JMenuItem(new MenuActions.NewAction(this, hacklaceConfigManager)));
		menuFile.add(new JMenuItem(new MenuActions.OpenAction(this, hacklaceConfigManager)));
		menuFile.add(new JMenuItem(new MenuActions.SaveAction(this, hacklaceConfigManager)));
		menuFile.add(new JMenuItem(new MenuActions.SaveAsAction(this, hacklaceConfigManager)));
		menuFile.add(new JMenuItem(new MenuActions.FlashAction(this, hacklaceConfigManager)));
		menuFile.add(new JMenuItem(new MenuActions.ExportBinAction(hacklaceConfigManager)));
		// menuFile.add(new JMenuItem(new MenuActions.ExportGifAction()));
		menuFile.add(new JMenuItem(new MenuActions.CloseAction(this)));
		menuBar.add(menuFile);
		// view menu
		JMenu menuView = new JMenu("View");
		ButtonGroup menuLedGroup = new ButtonGroup();
		JRadioButtonMenuItem menuLedGreen = new JRadioButtonMenuItem(new MenuActions.LedColorAction(this, "Green leds", Color.green));
		menuLedGreen.setSelected(true);
		menuLedGroup.add(menuLedGreen);
		menuView.add(menuLedGreen);
		JRadioButtonMenuItem menuLedRed = new JRadioButtonMenuItem(new MenuActions.LedColorAction(this, "Red leds", Color.red));
		menuLedGroup.add(menuLedRed);
		menuView.add(menuLedRed);
		JRadioButtonMenuItem menuLedBlack = new JRadioButtonMenuItem(new MenuActions.LedColorAction(this, "Black leds", Color.black));
		menuLedGroup.add(menuLedBlack);
		menuView.add(menuLedBlack);
		menuBar.add(menuView);
		// help menu
		JMenu menuHelp = new JMenu("Help");
		menuHelp.add(new JMenuItem(new MenuActions.HelpAction(this)));
		menuHelp.add(new JMenuItem(new MenuActions.LoadExampleAction(this, hacklaceConfigManager)));
		menuHelp.add(new JMenuItem(new MenuActions.LoadDefaultAction(this, hacklaceConfigManager)));
		menuBar.add(menuHelp);
		//
		setJMenuBar(menuBar);

		contentPane = super.getContentPane();
		homePanel = new HomePanel(hacklaceConfigManager, this);
		contentPane.add(homePanel);

		// Set stuff
		setTitle(title);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setPreferredSize(new Dimension(AnimatorGui.getIniConf().displayWidth(),
				AnimatorGui.getIniConf().displayHeight()));

		pack();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new MenuActions.CloseAction(AnimatorGui.this).actionPerformed(null);
			}
		});

	}

	public void endEditMode() {
		if (editPanel != null) {
			contentPane.remove(editPanel);
			editPanel.close();
			editPanel = null;
		}
		homePanel.setVisible(true);
		homePanel.updateSizeInfoLabel();
		repaint();
	}

	public void startEditMode(DisplayBuffer displayBuffer) {
		editPanel = EditPanel.factory(displayBuffer, homePanel, this, hacklaceConfigManager);
		if (editPanel == null) {
			JOptionPane
					.showMessageDialog(
							null,
							"This type of animation cannot be edited or is not supported yet.",
							"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		editPanel.setLedColor(ledColor);
		homePanel.setVisible(false);
		contentPane.add(editPanel);
	}

	public void loadFile(String fileName, ErrorContainer errorContainer) {
		try {
			File file = new File(fileName);
			hacklaceConfigManager.readFile(file, errorContainer);
			homePanel.updateList(hacklaceConfigManager.getList(), false);
			setCurrentFile(file);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null,
					"Cannot read from file. " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			if (errorContainer.containsFailure()) {
				JOptionPane.showMessageDialog(null, errorContainer.toString(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	public static IniConf getIniConf() {
		return iniConf;
	}
	
	public void setLedColor(Color color) {
		this.ledColor = color;
		if (editPanel != null) {
			editPanel.setLedColor(color);
		}
	}
}
