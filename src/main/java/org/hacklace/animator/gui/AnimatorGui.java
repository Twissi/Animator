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
	
	public AnimatorGui() {
		hacklaceConfigManager = new HacklaceConfigManager();
		initComponents();
		setVisible(true);
	}
	
	public AnimatorGui(final String fileName) {
		this();
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						loadFile(fileName);
					}
				}
		);
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
		tabs = new JTabbedPane();
		homePanel = new HomePanel();
		tabs.addTab("Home", null, homePanel, "Home");
		editAnimationPanel = new EditAnimationPanel();
		tabs.addTab("Edit(Animation)", null, editAnimationPanel, "Edit");		
		tabs.addTab("Edit(Text)", null, new EditTextPanel(), "Edit");
		ChangeListener changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				if (index > 0) {
					new FileActions.StartEditAction().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_FIRST, "Edit"));
				}
			}
		};
		tabs.addChangeListener(changeListener);
		
		// Add components
		add(tabs);
		
		// Set stuff
		setTitle( "The chosen Hacklace Animator Tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 700);
		
	}
	
	public HacklaceConfigManager getHacklaceConfigManager() {
		return hacklaceConfigManager;
	}
	
	/**
	 * Singleton
	 * @return
	 */
	public static AnimatorGui getInstance() {
	  if (instance == null) {
		  instance = new AnimatorGui();
	  }
	  return instance;
	}

	/**
	 * Singleton which can be used to directly load an animation file whose name was passed on the command line
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
			JOptionPane.showMessageDialog(null, "Cannot read from file. Message: " + ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}  catch (IllegalHacklaceConfigFileException ex) {
			JOptionPane.showMessageDialog(null, "Illegal hacklace config file. Message: " + ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
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
