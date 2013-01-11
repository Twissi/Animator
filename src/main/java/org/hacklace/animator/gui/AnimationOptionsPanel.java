package org.hacklace.animator.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

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
import org.hacklace.animator.enums.StepWidth;
import org.hacklace.animator.gui.actions.SaveAnimationAction;

public class AnimationOptionsPanel extends JPanel implements ChangeListener {
	private static final long serialVersionUID = -2625306373507959134L;
	private JSlider speedSlider;
	private JSlider delaySlider;
	private JRadioButton directionUni;
	private JRadioButton directionBi;
	private ButtonGroup directionButtons;
	private JRadioButton stepOne;
	private JRadioButton stepFive;
	private ButtonGroup stepButtons;
	private EditPanel editPanel;

	public AnimationOptionsPanel(EditPanel editPanel) {
		this.editPanel = editPanel;
		removeAll();
		initComponents(editPanel);
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

	private JPanel createStepWidthPanel() {
		JPanel stepWidthPanel = new JPanel();
		stepWidthPanel.add(new JLabel("StepWidth:"));
		stepButtons = new ButtonGroup();
		stepOne = new JRadioButton("1");
		stepOne.addChangeListener(this);
		stepButtons.add(stepOne);
		stepWidthPanel.add(stepOne);
		stepFive = new JRadioButton("5");
		stepFive.addChangeListener(this);
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

	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(speedSlider)) {

			int intSpeed = ((JSlider) e.getSource()).getValue();
			editPanel.onSpeedChanged(Speed.fromInt(intSpeed));

		} else if (e.getSource().equals(delaySlider)) {

			int intDelay = ((JSlider) e.getSource()).getValue();
			editPanel.onDelayChanged(Delay.fromInt(intDelay));

		} else if (e.getSource().equals(directionUni)
				|| e.getSource().equals(directionBi)) {

			if (directionUni.isSelected()) {
				editPanel.onDirectionChanged(Direction.FORWARD);
			} else {
				editPanel.onDirectionChanged(Direction.BIDIRECTIONAL);
			}

		} else if (e.getSource().equals(stepOne)
				|| e.getSource().equals(stepFive)) {

			if (stepOne.isSelected()) {
				editPanel.onStepChanged(StepWidth.ONE);
			} else {
				editPanel.onStepChanged(StepWidth.FIVE);
			}

		}
	}

	private void initComponents(EditPanel editPanel) {
		// one column grid layout
		setLayout(new GridLayout(0, 1));
		add(new JLabel("Options"));
		add(new JLabel("Speed:"));
		speedSlider = new JSlider(SwingConstants.HORIZONTAL,
				Speed.values().length - 1);
		speedSlider.setPaintTicks(true);
		speedSlider.setSnapToTicks(true);
		speedSlider.setMinorTickSpacing(1);
		speedSlider.addChangeListener(this);
		add(speedSlider);
		add(new JLabel("Delay:"));
		delaySlider = new JSlider(SwingConstants.HORIZONTAL,
				Delay.values().length - 1);
		delaySlider.setPaintTicks(true);
		delaySlider.setSnapToTicks(true);
		delaySlider.setMinorTickSpacing(1);
		delaySlider.addChangeListener(this);
		add(delaySlider);
		add(createDirectionPanel());
		add(createStepWidthPanel());
		JButton saveButton = new JButton(new SaveAnimationAction(editPanel));
		add(saveButton);
		JButton cancelButton = new JButton(new CancelEditAction());
		add(cancelButton);
	}

	class CancelEditAction extends AbstractAction {
		private static final long serialVersionUID = 8730578405697706858L;

		public CancelEditAction() {
			super("Cancel");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			AnimatorGui.getInstance().endEditMode();
		}
	}
}
