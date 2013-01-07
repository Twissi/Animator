package org.hacklace.animator.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.gui.actions.AnimationListActions;

public class HomePanel extends JPanel {
	private static final long serialVersionUID = 1045750321890262891L;

	JList animationList;
	// Java 7: JList<DisplayBuffer> animationList;
	DefaultListModel animationListData;
	// Java 7: DefaultListModel<DisplayBuffer> animationListData;
	
	private JLabel infoLabel;
	private HacklaceConfigManager hacklaceConfigManager;

	public HomePanel(HacklaceConfigManager hcm,
			AnimatorGui animatorGui) {
		hacklaceConfigManager = hcm;
		animationListData = new DefaultListModel();
		// Java 7: animationListData = new DefaultListModel<DisplayBuffer>();
		animationList = new JList(animationListData);
		animationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Java 7: animationList = new JList<DisplayBuffer>(animationListData);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 3;
		JScrollPane animationListScrollPane = new JScrollPane(animationList);
		animationListScrollPane.setPreferredSize(new Dimension(400, 200));
		add(animationListScrollPane, c);
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JButton(new AnimationListActions.StartEditAction(this,
				hacklaceConfigManager, animatorGui)), c);
		add(new JButton(new AnimationListActions.MoveUpAction(this,
				hacklaceConfigManager)), c);
		add(new JButton(new AnimationListActions.MoveDownAction(this,
				hacklaceConfigManager)), c);
		add(new JButton(new AnimationListActions.RemoveAction(this,
				hacklaceConfigManager)), c);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		add(new JButton(new AnimationListActions.AddAction(this,
				hacklaceConfigManager)), c);
		infoLabel = new JLabel("");
		add(infoLabel, c);
		updateInfoLabel();
	}
	
	public void updateInfoLabel()  {
		int bytesUsed = hacklaceConfigManager.getNumBytes();
		int maxBytes = IniConf.getInstance().maxBytes();
		if (bytesUsed > maxBytes) infoLabel.setForeground(Color.red);
		else infoLabel.setForeground(Color.black);
		infoLabel.setText(bytesUsed + " / " + maxBytes + " Bytes used.");
	}

	/**
	 * Update the list contents
	 * 
	 * @param items
	 *            list of strings to select from
	 */
	public void updateList(List<DisplayBuffer> items, boolean keepIndex) {
		int index = animationList.getSelectedIndex();
		animationListData.removeAllElements();
		for (DisplayBuffer item : items) {
			animationListData.addElement(item);
		}
		if (keepIndex)
			animationList.setSelectedIndex(index);
		updateInfoLabel();
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
	 * 
	 * @return int the old index
	 */
	public int moveUp() {
		int index = animationList.getSelectedIndex();
		if (index < 1)
			return -1;
		DisplayBuffer tmp = (DisplayBuffer) animationListData.get(index);
		animationListData.remove(index);
		animationListData.add(index - 1, tmp);
		animationList.setSelectedIndex(index - 1);
		animationList.ensureIndexIsVisible(index - 1);
		return index;
	}

	/**
	 * Moves the currently selected item 1 position down
	 * 
	 * @return int the old index
	 */
	public int moveDown() {
		int index = animationList.getSelectedIndex();
		if (index > animationListData.size() - 2)
			return -1;
		DisplayBuffer tmp = (DisplayBuffer) animationListData.get(index);
		animationListData.remove(index);
		animationListData.add(index + 1, tmp);
		animationList.setSelectedIndex(index + 1);
		animationList.ensureIndexIsVisible(index + 1);
		return index;
	}

	public int removeCurrent() {
		int index = animationList.getSelectedIndex();
		if (index == -1)
			return -1;
		animationListData.remove(index);
		animationList.setSelectedIndex(index);
		animationList.ensureIndexIsVisible(index);
		return index;
	}
	
	public boolean isValidSelection() {
		int index = animationList.getSelectedIndex();
		return (index != -1);
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
