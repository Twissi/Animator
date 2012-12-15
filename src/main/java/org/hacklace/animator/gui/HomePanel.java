package org.hacklace.animator.gui;

import java.awt.Dimension;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.hacklace.animator.displaybuffer.DisplayBuffer;

public class HomePanel extends JPanel {
	private static final long serialVersionUID = 1045750321890262891L;

	JList<DisplayBuffer> animationList;
	DefaultListModel<DisplayBuffer> animationListData;

	public HomePanel() {
		animationListData = new DefaultListModel<DisplayBuffer>();
		animationList = new JList<DisplayBuffer>(animationListData);
		JScrollPane animationListScrollPane = new JScrollPane(animationList);
		animationListScrollPane.setPreferredSize(new Dimension(400, 200));
		add(animationListScrollPane);
		
		add(new JButton(new AnimationListActions.AddAction()));
		add(new JButton(new AnimationListActions.RemoveAction()));
		add(new JButton(new AnimationListActions.MoveUpAction()));
		add(new JButton(new AnimationListActions.MoveDownAction()));
		add(new JButton(new FileActions.StartEditAction()));
	}
	
	/**
	 * Update the list contents
	 * @param items list of strings to select from
	 */
	public void updateList(List<DisplayBuffer> items) {
		animationListData.removeAllElements();
		for (DisplayBuffer item: items) {
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
	 * @return int the old index
	 */
	public int moveUp() {
		int index = animationList.getSelectedIndex();
		if (index < 1) return -1;
		DisplayBuffer tmp = (DisplayBuffer)animationListData.get(index);
		animationListData.remove(index);
		animationListData.add(index - 1, tmp);
		animationList.setSelectedIndex(index - 1);
		animationList.ensureIndexIsVisible(index - 1);
		return index;
	}
	
	/**
	 * Moves the currently selected item 1 position down
	 * @return int the old index
	 */
	public int moveDown() {
		int index = animationList.getSelectedIndex();
		if (index > animationListData.size() - 2) return -1;
		DisplayBuffer tmp = (DisplayBuffer)animationListData.get(index);
		animationListData.remove(index);
		animationListData.add(index + 1, tmp);
		animationList.setSelectedIndex(index + 1);
		animationList.ensureIndexIsVisible(index + 1);
		return index;
	}
	
	public void removeCurrent() {
		int index = animationList.getSelectedIndex();
		if (index == -1) return;
		animationListData.remove(index);
		animationList.setSelectedIndex(index);
		animationList.ensureIndexIsVisible(index);
	}
	
	public void add(DisplayBuffer s) {
		add(s, true);
	}
	
	public void add(DisplayBuffer s, boolean atEnd) {
		int index;
		if (atEnd) {
			 index = animationListData.getSize() - 1;
		} else {
			index = animationList.getSelectedIndex();
		}
		animationListData.add(index + 1, s);
		animationList.setSelectedIndex(index + 1);
		animationList.ensureIndexIsVisible(index + 1);
	}
	
	public int getSelectedIndex() {
		return animationList.getSelectedIndex();
	}
}
