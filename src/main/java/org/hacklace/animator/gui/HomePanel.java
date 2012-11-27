package org.hacklace.animator.gui;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

public class HomePanel extends JPanel {
	private static final long serialVersionUID = 1045750321890262891L;

	JList<String> animationList;
	DefaultListModel animationListData;

	public HomePanel() {
		animationListData = new DefaultListModel();
		animationList = new JList<String>(animationListData);
		JScrollPane animationListScrollPane = new JScrollPane(animationList);
		// TODO: not working :( animationList.setMinimumSize(new Dimension(200, 50));
		add(animationListScrollPane);
		
		add(new JButton(new AnimationListActions.AddAction()));
		add(new JButton(new AnimationListActions.RemoveAction()));
		add(new JButton(new AnimationListActions.MoveUpAction()));
		add(new JButton(new AnimationListActions.MoveDownAction()));
	}
	
	/**
	 * Update the list contents
	 * @param items list of strings to select from
	 */
	public void updateList(List<String> items) {
		animationListData.removeAllElements();
		for (String item: items) {
			animationListData.addElement(item);
		}
	}
	
	/**
	 * Clear the list
	 */
	public void clear() {
		animationListData.clear();
	}
	
	/**
	 * reset the panel to its default data
	 */
	public void reset() {
		clear();
	}
	
	/**
	 * Moves the currently selected item 1 position up
	 */
	public void moveUp() {
		int index = animationList.getSelectedIndex();
		if (index < 1) return;
		String tmp = (String)animationListData.get(index);
		animationListData.remove(index);
		animationListData.add(index - 1, tmp);
		animationList.setSelectedIndex(index - 1);
	}
	
	/**
	 * Moves the currently selected item 1 position down
	 */
	public void moveDown() {
		int index = animationList.getSelectedIndex();
		if (index > animationListData.size() - 2) return;
		String tmp = (String)animationListData.get(index);
		animationListData.remove(index);
		animationListData.add(index + 1, tmp);
		animationList.setSelectedIndex(index + 1);
	}
	
	public void removeCurrent() {
		int index = animationList.getSelectedIndex();
		if (index == -1) return; // @TODO check if actually "-1" means no selection
		animationListData.remove(index);
		animationList.setSelectedIndex(index);
	}
	
	public void add(String s) {
		int index = animationList.getSelectedIndex();
		animationListData.add(index + 1, s);
		animationList.setSelectedIndex(index + 1);
	}
}
