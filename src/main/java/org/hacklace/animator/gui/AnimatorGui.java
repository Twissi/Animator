package org.hacklace.animator.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.IllegalHacklaceConfigFileException;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.displaybuffer.DisplayBuffer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;

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

	public AnimatorGui(final String fileName) {
		this();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				loadFile(fileName);
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
		menuFile.add(new JMenuItem(new MenuActions.OpenAction()));
		menuFile.add(new JMenuItem(new MenuActions.SaveAction()));
	 	menuFile.add(new JMenuItem(new MenuActions.SaveAsAction()));
		menuFile.add(new JMenuItem(new MenuActions.FlashAction()));
		menuFile.add(new JMenuItem(new MenuActions.ExportBinAction()));
		menuFile.add(new JMenuItem(new MenuActions.ExportGifAction()));
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(IniConf.getInstance().displayWidth(), IniConf.getInstance().displayHeight()));

		pack();

	}
	
	public HacklaceConfigManager getHacklaceConfigManager() {
		return hacklaceConfigManager;
	}

	public void stopEditMode() {
		if (editPanel != null) {
			contentPane.remove(editPanel);
			editPanel = null;
		}
		homePanel.setVisible(true);
		repaint();
	}
	
	public void startEditMode(DisplayBuffer displayBuffer) {
		editPanel = EditPanel.factory(displayBuffer);
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

	public void loadFile(String fileName) {
		try {
			File file = new File(fileName);
			hacklaceConfigManager.readFile(file);
			homePanel.updateList(hacklaceConfigManager.getList(), false);
			setCurrentFile(file);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null,
					"Cannot read from file. Message: " + ex.toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalHacklaceConfigFileException ex) {
			JOptionPane.showMessageDialog(null,
					"Illegal hacklace config file. Message: " + ex.toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
