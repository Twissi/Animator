package org.hacklace.animator.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import java.io.File;


public class AnimatorGUI extends JFrame {
	
	private static final long serialVersionUID = 2757544085601062109L;
	
	public static AnimatorGUI appInstance;
	
	public static final int ROWS = 7;
	public static final int COLUMNS = 5;
	
	private File currentFile;
	
	private HomePanel homePanel;
	private EditAnimationPanel editAnimationPanel;
	
	public AnimatorGUI() {
		initComponents();
		setVisible(true);
	}
	
	public File getCurrentFile() {
		return currentFile;
	}
	
	public void setCurrentFile(File file) {
		currentFile = file;
	}
	
	public HomePanel getHomePanel() {
		return homePanel;
	}
	
	public EditAnimationPanel getEditAnimationPanel() {
		return editAnimationPanel;
	}
	
	private void initComponents() {
		
		// main menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		menuFile.add(new JMenuItem(new FileActions.OpenAction()));
		menuFile.add(new JMenuItem(new FileActions.SaveAction()));
		menuFile.add(new JMenuItem(new FileActions.CloseAction()));
		menuBar.add(menuFile);
		setJMenuBar(menuBar);

		// tabs
		JTabbedPane tabs = new JTabbedPane();
		homePanel = new HomePanel();
		tabs.addTab("Home", null, homePanel, "Home");
		editAnimationPanel = new EditAnimationPanel();
		tabs.addTab("Edit(Animation)", null, editAnimationPanel, "Edit");		
		tabs.addTab("Edit(Text)", null, new EditTextPanel(), "Edit");		
		
		// Add components
		add(tabs);
		
		// Set stuff
		setTitle( "The chosen Hacklace Animator Tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 700);
		
	}

	public static void main(String[] args) {
		appInstance = new AnimatorGUI();
	}
	

}
