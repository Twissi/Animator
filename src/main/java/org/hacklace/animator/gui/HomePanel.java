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

	private JLabel sizeInfoLabel;
	private HacklaceConfigManager configManager;

	public HomePanel(HacklaceConfigManager hcm, AnimatorGui animatorGui) {
		configManager = hcm;
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
		c.gridheight = 5;
		JScrollPane animationListScrollPane = new JScrollPane(animationList);
		animationListScrollPane.setPreferredSize(new Dimension(400, 200));
		add(animationListScrollPane, c);
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JButton(new AnimationListActions.StartEditAction(this,
				animatorGui)), c);
		add(new JButton(new AnimationListActions.MoveUpAction(this)), c);
		add(new JButton(new AnimationListActions.MoveDownAction(this)), c);
		add(new JButton(new AnimationListActions.CopyAnimationAction(this)), c);
		add(new JButton(new AnimationListActions.RemoveAction(this)), c);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		add(new JButton(new AnimationListActions.AddAction(this)), c);
		sizeInfoLabel = new JLabel("");
		add(sizeInfoLabel, c);
		updateSizeInfoLabel();
	}

	public void updateSizeInfoLabel() {
		int bytesUsed = configManager.getNumBytes();
		int maxBytes = IniConf.getInstance().maxBytes();
		if (bytesUsed > maxBytes)
			sizeInfoLabel.setForeground(Color.red);
		else
			sizeInfoLabel.setForeground(Color.black);
		sizeInfoLabel.setText(bytesUsed + " / " + maxBytes + " Bytes used.");
	}

	public void updateList(boolean keepIndex) {
		updateList(configManager.getList(), keepIndex);
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
		updateSizeInfoLabel();
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
	public void moveUp() {
		int index = animationList.getSelectedIndex();
		if (index < 1)
			return;
		DisplayBuffer tmp = (DisplayBuffer) animationListData.get(index);
		animationListData.remove(index);
		animationListData.add(index - 1, tmp);
		animationList.setSelectedIndex(index - 1);
		animationList.ensureIndexIsVisible(index - 1);

		configManager.moveUp(index);
		updateList(true);
	}

	/**
	 * Moves the currently selected item 1 position down
	 * 
	 * @return int the old index
	 */
	public void moveDown() {
		int index = animationList.getSelectedIndex();
		if (index > animationListData.size() - 2)
			return;
		DisplayBuffer tmp = (DisplayBuffer) animationListData.get(index);
		animationListData.remove(index);
		animationListData.add(index + 1, tmp);
		animationList.setSelectedIndex(index + 1);
		animationList.ensureIndexIsVisible(index + 1);
		
		configManager.moveUp(index);
		updateList(true);
	}

	public void removeCurrent() {
		int index = animationList.getSelectedIndex();
		if (index == -1)
			return;
		animationListData.remove(index);
		animationList.setSelectedIndex(index);
		animationList.ensureIndexIsVisible(index);
		configManager.deleteDisplayBuffer(index);
		updateList(true);
	}

	public boolean isValidSelection() {
		int index = animationList.getSelectedIndex();
		return (index != -1);
	}

	public void add(DisplayBuffer displayBuffer) {
		add(displayBuffer, true);
	}

	public void add(DisplayBuffer displayBuffer, boolean atEnd) {
		int index;
		if (atEnd) {
			index = animationListData.getSize() - 1;
		} else {
			index = animationList.getSelectedIndex();
		}

		configManager.addDisplayBuffer(displayBuffer, index + 1);

		animationListData.add(index + 1, displayBuffer);
		animationList.setSelectedIndex(index + 1);
		animationList.ensureIndexIsVisible(index + 1);

		updateList(true);
	}

	public int getSelectedIndex() {
		return animationList.getSelectedIndex();
	}

	public DisplayBuffer getSelectedBuffer() {
		int index = getSelectedIndex();
		return configManager.getDisplayBuffer(index);
	}

}
