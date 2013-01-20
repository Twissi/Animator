/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
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
import org.hacklace.animator.gui.actions.OptionsObserver;
import org.hacklace.animator.gui.actions.SaveAnimationAction;
import org.hacklace.animator.gui.actions.SaveObserver;
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
	private AnimatorGui animatorGui;

	public AnimationOptionsPanel(OptionsObserver optionsObserver, SaveObserver saveObserver, AnimatorGui animatorGui) {
		this.animatorGui = animatorGui;
		removeAll();
		initComponents(optionsObserver, saveObserver);
	}

	private JPanel createDirectionPanel(OptionsObserver optionsObserver) {
		JPanel directionPanel = new JPanel();
		directionPanel.add(new JLabel("Direction:"));
		directionButtons = new ButtonGroup();
		directionUni = new JRadioButton("Unidirectional");
		directionUni.addActionListener(new DirectionListener(Direction.FORWARD,
				optionsObserver));
		directionButtons.add(directionUni);
		directionPanel.add(directionUni);
		directionBi = new JRadioButton("Bidirectional");
		directionBi.addActionListener(new DirectionListener(Direction.BIDIRECTIONAL,
				optionsObserver));
		directionButtons.add(directionBi);
		directionPanel.add(directionBi);

		return directionPanel;
	}

	private JPanel createStepWidthPanel(OptionsObserver optionsObserver) {
		JPanel stepWidthPanel = new JPanel();
		stepWidthPanel.add(new JLabel("StepWidth:"));
		stepButtons = new ButtonGroup();
		stepOne = new JRadioButton("1");
		stepOne.addActionListener(new StepWidthListener(StepWidth.ONE,
				optionsObserver));
		stepButtons.add(stepOne);
		stepWidthPanel.add(stepOne);
		stepFive = new JRadioButton("5");
		stepFive.addActionListener(new StepWidthListener(StepWidth.FIVE,
				optionsObserver));
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

	private void initComponents(OptionsObserver optionsObserver, SaveObserver saveObserver) {
		// one column grid layout
		setLayout(new GridLayout(0, 1));
		add(new JLabel("Options"));
		add(new JLabel("Speed:"));
		speedSlider = new JSlider(0, Speed.values().length - 1);
		speedSlider.setPaintTicks(true);
		speedSlider.setSnapToTicks(true);
		speedSlider.setMinorTickSpacing(1);
		speedSlider.addChangeListener(new SpeedChangeListener(speedSlider,
				optionsObserver));
		add(speedSlider);
		add(new JLabel("Delay:"));
		delaySlider = new JSlider(0, Delay.values().length - 1);
		delaySlider.setPaintTicks(true);
		delaySlider.setSnapToTicks(true);
		delaySlider.setMinorTickSpacing(1);
		delaySlider.addChangeListener(new DelayChangeListener(delaySlider, optionsObserver));
		add(delaySlider);
		add(createDirectionPanel(optionsObserver));
		add(createStepWidthPanel(optionsObserver));
		JButton saveButton = new JButton(new SaveAnimationAction(saveObserver));
		add(saveButton);
		JButton cancelButton = new JButton(new CancelEditAction(animatorGui));
		add(cancelButton);
	}
}
