package org.hacklace.animator.gui;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.enums.StepWidth;
import org.hacklace.animator.gui.actions.CancelEditAction;
import org.hacklace.animator.gui.actions.DelayChangeListener;
import org.hacklace.animator.gui.actions.DirectionListener;
import org.hacklace.animator.gui.actions.SaveAnimationAction;
import org.hacklace.animator.gui.actions.SpeedChangeListener;
import org.hacklace.animator.gui.actions.StepWidthListener;

public class AnimationOptionsPanel extends JPanel {
	private static final long serialVersionUID = -2625306373507959134L;
	private JSlider speedSlider;
	private JSlider delaySlider;
	private JRadioButton directionUni;
	private JRadioButton directionBi;
	private ButtonGroup directionButtons;
	private JRadioButton stepOne;
	private JRadioButton stepFive;
	private ButtonGroup stepButtons;

	public AnimationOptionsPanel(EditPanel editPanel) {
		removeAll();
		initComponents(editPanel);
	}

	private JPanel createDirectionPanel(EditPanel editPanel) {
		JPanel directionPanel = new JPanel();
		directionPanel.add(new JLabel("Direction:"));
		directionButtons = new ButtonGroup();
		directionUni = new JRadioButton("Unidirectional");
		directionUni.addActionListener(new DirectionListener(editPanel,
				Direction.FORWARD));
		directionButtons.add(directionUni);
		directionPanel.add(directionUni);
		directionBi = new JRadioButton("Bidirectional");
		directionBi.addActionListener(new DirectionListener(editPanel,
				Direction.BIDIRECTIONAL));
		directionButtons.add(directionBi);
		directionPanel.add(directionBi);

		return directionPanel;
	}

	private JPanel createStepWidthPanel(EditPanel editPanel) {
		JPanel stepWidthPanel = new JPanel();
		stepWidthPanel.add(new JLabel("StepWidth:"));
		stepButtons = new ButtonGroup();
		stepOne = new JRadioButton("1");
		stepOne.addActionListener(new StepWidthListener(editPanel,
				StepWidth.ONE));
		stepButtons.add(stepOne);
		stepWidthPanel.add(stepOne);
		stepFive = new JRadioButton("5");
		stepFive.addActionListener(new StepWidthListener(editPanel,
				StepWidth.FIVE));
		stepButtons.add(stepFive);
		stepWidthPanel.add(stepFive);
		return stepWidthPanel;
	}

	public void setOptions(Speed speed, Delay delay, Direction direction,
			StepWidth step) {
		speedSlider.setValue(speed.getValue());
		delaySlider.setValue(delay.getValue());
		if (direction == Direction.FORWARD) {
			directionUni.setSelected(true);
		} else {
			directionBi.setSelected(true);
		}
		if (step == StepWidth.ONE) {
			stepOne.setSelected(true);
		} else {
			stepFive.setSelected(true);
		}
	}

	private void initComponents(EditPanel editPanel) {
		// one column grid layout
		setLayout(new GridLayout(0, 1));
		add(new JLabel("Options"));
		add(new JLabel("Speed:"));
		speedSlider = new JSlider(0, Speed.values().length - 1);
		speedSlider.setPaintTicks(true);
		speedSlider.setSnapToTicks(true);
		speedSlider.setMinorTickSpacing(1);
		speedSlider.addChangeListener(new SpeedChangeListener(editPanel,
				speedSlider));
		add(speedSlider);
		add(new JLabel("Delay:"));
		delaySlider = new JSlider(0, Delay.values().length - 1);
		delaySlider.setPaintTicks(true);
		delaySlider.setSnapToTicks(true);
		delaySlider.setMinorTickSpacing(1);
		delaySlider.addChangeListener(new DelayChangeListener(editPanel, delaySlider));
		add(delaySlider);
		add(createDirectionPanel(editPanel));
		add(createStepWidthPanel(editPanel));
		JButton saveButton = new JButton(new SaveAnimationAction(editPanel));
		add(saveButton);
		JButton cancelButton = new JButton(new CancelEditAction());
		add(cancelButton);
	}
}
