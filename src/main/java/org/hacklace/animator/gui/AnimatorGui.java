package org.hacklace.animator.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.IllegalHacklaceConfigFileException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;

public class AnimatorGui extends JFrame {

	private static final long serialVersionUID = 2757544085601062109L;

	public static AnimatorGui instance;

	public static final int ROWS = 7;
	public static final int COLUMNS = 5;

	private File currentFile;

	private HomePanel homePanel;
	private EditAnimationPanel editAnimationPanel;

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

	public EditAnimationPanel getEditAnimationPanel() {
		return editAnimationPanel;
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

		homePanel = new HomePanel(hacklaceConfigManager, this);
		add(homePanel);
		editAnimationPanel = new EditAnimationPanel();
		add(editAnimationPanel);
		
		setEditMode(false);
		
		// Set stuff
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(900, 500));

		pack();

	}
	
	public void setEditMode(boolean editMode) {
		if (editMode) {
			editAnimationPanel.setVisible(true);
			homePanel.setVisible(false);
		} else {
			editAnimationPanel.setVisible(false);
			homePanel.setVisible(true);
		}
	}

	public HacklaceConfigManager getHacklaceConfigManager() {
		return hacklaceConfigManager;
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

	public static void main(String[] args) {
		if (args.length > 0) {
			getInstance(args[0]);
		} else {
			getInstance();
		}
	}

}
