package org.hacklace.animator.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.IllegalHacklaceConfigFileException;

import java.awt.event.ActionEvent;
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
	private JTabbedPane tabs;

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

	public int getCurrentTabIndex() {
		return tabs.getSelectedIndex();
	}

	public void setCurrentTabIndex(int index) {
		tabs.setSelectedIndex(index);
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

		JMenuBar menuBar = new JMenuBar();
		// file menu
		JMenu menuFile = new JMenu("File");
		menuFile.add(new JMenuItem(new MenuActions.OpenAction()));
		menuFile.add(new JMenuItem(new MenuActions.SaveAction()));
	 	menuFile.add(new JMenuItem(new MenuActions.SaveAsAction()));
		menuFile.add(new JMenuItem(new MenuActions.CloseAction()));
		menuBar.add(menuFile);
		// help menu
		JMenu menuHelp = new JMenu("Help");
		menuHelp.add(new JMenuItem("Launch Help"));
		menuBar.add(menuHelp);
		// 
		setJMenuBar(menuBar);

		// tabs
		tabs = new JTabbedPane();
		homePanel = new HomePanel();
		tabs.addTab("Home", null, homePanel, "Home");
		editAnimationPanel = new EditAnimationPanel();
		tabs.addTab("Edit(Animation)", null, editAnimationPanel, "Edit");
		ChangeListener changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent
						.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				if (index > 0) {
					new AnimationListActions.StartEditAction()
							.actionPerformed(new ActionEvent(this,
									ActionEvent.ACTION_FIRST, "Edit"));
				}
			}
		};
		tabs.addChangeListener(changeListener);

		// Add components
		add(tabs);

		// Set stuff
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 700);

		pack();

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
			homePanel.updateList(hacklaceConfigManager.getList());
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
