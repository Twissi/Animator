package org.hacklace.animator.gui;

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
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.gui.actions.MenuActions;

public class AnimatorGui extends JFrame {

	private static final long serialVersionUID = 2757544085601062109L;

	public static AnimatorGui instance;

	private File currentFile;

	private HomePanel homePanel;
	private EditPanel editPanel = null;
	private Container contentPane;

	private HacklaceConfigManager hacklaceConfigManager;

	private String title = "The chosen Hacklace Animator Tool";

	public AnimatorGui() {
		hacklaceConfigManager = new HacklaceConfigManager();
		initComponents();
		setVisible(true);
	}

	public void showHelp() {
		URL url = HelpSet.findHelpSet(null, "help/jhelpset.hs");
		JHelp helpViewer = null;
		try {
			helpViewer = new JHelp(new HelpSet(null, url));
			// helpViewer.setCurrentID("...");
			JFrame frame = new JFrame();
			frame.setTitle("Hacklace Animator Help");
			frame.setSize(800, 600);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
			frame.getContentPane().add(helpViewer);
		} catch (HelpSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AnimatorGui(final String fileName) {
		this();
		final ErrorContainer errorContainer = new ErrorContainer();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				loadFile(fileName, errorContainer);
			}
		});
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
		menuFile.add(new JMenuItem(new MenuActions.NewAction()));
		menuFile.add(new JMenuItem(new MenuActions.OpenAction()));
		menuFile.add(new JMenuItem(new MenuActions.SaveAction()));
		menuFile.add(new JMenuItem(new MenuActions.SaveAsAction()));
		menuFile.add(new JMenuItem(new MenuActions.FlashAction()));
		menuFile.add(new JMenuItem(new MenuActions.ExportBinAction()));
		// menuFile.add(new JMenuItem(new MenuActions.ExportGifAction()));
		menuFile.add(new JMenuItem(new MenuActions.CloseAction()));
		menuBar.add(menuFile);
		// help menu
		JMenu menuHelp = new JMenu("Help");
		menuHelp.add(new JMenuItem(new MenuActions.HelpAction()));
		menuHelp.add(new JMenuItem(new MenuActions.LoadExampleAction()));
		menuHelp.add(new JMenuItem(new MenuActions.LoadDefaultAction()));
		menuBar.add(menuHelp);
		//
		setJMenuBar(menuBar);

		contentPane = this.getContentPane();
		homePanel = new HomePanel(hacklaceConfigManager, this);
		contentPane.add(homePanel);

		// Set stuff
		setTitle(title);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setPreferredSize(new Dimension(IniConf.getInstance().displayWidth(),
				IniConf.getInstance().displayHeight()));

		pack();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new MenuActions.CloseAction().actionPerformed(null);
			}
		});

	}

	public HacklaceConfigManager getHacklaceConfigManager() {
		return hacklaceConfigManager;
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
		editPanel = EditPanel.factory(displayBuffer);
		if (editPanel == null) {
			JOptionPane
					.showMessageDialog(
							null,
							"This type of animation cannot be edited or is not supported yet.",
							"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		homePanel.setVisible(false);
		contentPane.add(editPanel);
	}

	/**
	 * Singleton
	 * 
	 * @return
	 */
	public static AnimatorGui getInstance() {
		if (instance == null) {
			instance = new AnimatorGui();
		}
		return instance;
	}

	/**
	 * Singleton which can be used to directly load an animation file whose name
	 * was passed on the command line
	 * 
	 * @param fileName
	 * @return
	 */
	public static AnimatorGui getInstance(String fileName) {
		if (instance == null) {
			instance = new AnimatorGui(fileName);
		}
		return instance;
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
}
