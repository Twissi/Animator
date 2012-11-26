package org.hacklace.animator.gui;

import javax.swing.JFrame;


public class AnimatorGUI extends JFrame {
	
	private static final long serialVersionUID = 2757544085601062109L;
	
	public static final int ROWS = 7;
	public static final int COLUMNS = 5;
	
	public AnimatorGUI() {
		initComponents();
		setVisible(true);
	}
	
	private void initComponents() {		
		GridPanel gridPanel = new GridPanel(ROWS, COLUMNS);		
		
		// Add components
		add(gridPanel );
		
		
		// Set stuff
		setTitle( "The chosen Hacklace Animator Tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 700);
		
	}

	public static void main(String[] args) {
		new AnimatorGUI();
	}
	

}
