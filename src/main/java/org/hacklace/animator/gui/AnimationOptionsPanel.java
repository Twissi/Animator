package org.hacklace.animator.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;

public class AnimationOptionsPanel extends JPanel implements ChangeListener {
	private static final long serialVersionUID = -2625306373507959134L;
	private JSlider speedSlider;
	private JSlider delaySlider;
	private JRadioButton directionUni;
	private JRadioButton directionBi;
	private ButtonGroup directionButtons;
	private List<OptionsObserver> observerList;
	
	public AnimationOptionsPanel() {
		observerList = new ArrayList<OptionsObserver>();
		removeAll();
		initComponents();
	}
	
	private JPanel createDirectionPanel() {
		JPanel directionPanel = new JPanel();
		directionPanel.add(new JLabel("Direction:"));
		directionButtons = new ButtonGroup();
		directionUni = new JRadioButton("Unidirectional");
		directionUni.addChangeListener(this);
		directionButtons.add(directionUni);
		directionPanel.add(directionUni);
		directionBi = new JRadioButton("Bidirectional");
		directionBi.addChangeListener(this);
		directionButtons.add(directionBi);
		directionPanel.add(directionBi);
		return directionPanel;
	}
	
	
	public void setOptions(int speed, int delay, int direction) {
		speedSlider.setValue(speed);
		delaySlider.setValue(delay);
		if (direction == 0) {
			directionUni.setSelected(true);
		} else {
			directionBi.setSelected(true);
		}
	}
	
	public void addObserver(OptionsObserver o) {
		observerList.add(o);
	}
	
	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(speedSlider)) {
			for (OptionsObserver o: observerList) {
				int intSpeed = ((JSlider)e.getSource()).getValue();
				o.onSpeedChanged(Speed.fromInt(intSpeed));
			}
		} else if (e.getSource().equals(delaySlider)) {
			for (OptionsObserver o: observerList) {
				int intDelay = ((JSlider)e.getSource()).getValue();
				o.onDelayChanged(Delay.fromInt(intDelay));
			}
		} else if (e.getSource().equals(directionUni) || e.getSource().equals(directionBi)) {
			for (OptionsObserver o: observerList) {
				if (directionUni.isSelected()) {
					o.onDirectionChanged(Direction.FORWARD);
				} else {
					o.onDirectionChanged(Direction.BIDIRECTIONAL);
				}
			}
		}
	}

	private void initComponents() {
		// one column grid layout
		setLayout(new GridLayout(0,1));
		add(new JLabel("Options"));
		add(new JLabel("Speed:"));
		speedSlider = new JSlider(
				SwingConstants.HORIZONTAL, 
				Speed.values().length - 1
				);
		speedSlider.setPaintTicks(true);
		speedSlider.setSnapToTicks(true);
		speedSlider.setMinorTickSpacing(1);
		speedSlider.addChangeListener(this);
		add(speedSlider);
		add(new JLabel("Delay:"));
		delaySlider = new JSlider(
				SwingConstants.HORIZONTAL, 
				Delay.values().length - 1);
		delaySlider.setPaintTicks(true);
		delaySlider.setSnapToTicks(true);
		delaySlider.setMinorTickSpacing(1);
		delaySlider.addChangeListener(this);
		add(delaySlider);
		add(createDirectionPanel());
		JButton saveButton = new JButton(new SaveAnimationAction());
		add(saveButton);
		JButton cancelButton = new JButton(new CancelEditAction());
		add(cancelButton);
	}
	
	class SaveAnimationAction extends AbstractAction {
		private static final long serialVersionUID = -5813301123661228603L;
		public SaveAnimationAction() {
			super("Save");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			for (OptionsObserver o: observerList) {
				o.onSaveAnimation();
				AnimatorGui.getInstance().stopEditMode();
			}
		}
	}

	class CancelEditAction extends AbstractAction {
		private static final long serialVersionUID = 8730578405697706858L;
		public CancelEditAction() {
			super("Cancel");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			AnimatorGui.getInstance().stopEditMode();
		}
	}
}
