package org.hacklace.animator.gui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Speed;

public class AnimationOptionsPanel extends JPanel implements ChangeListener {
	private static final long serialVersionUID = -2625306373507959134L;
	private JSlider speedSlider;
	private JSlider delaySlider;
	private JSlider positionSlider;
	private ButtonGroup animationType;
	private List<OptionsObserver> observerList;
	
	public AnimationOptionsPanel() {
		observerList = new ArrayList<OptionsObserver>();
		removeAll();
		initComponents();
	}
	
	public void setOptions(int speed, int delay) {
		speedSlider.setValue(speed);
		delaySlider.setValue(delay);
	}
	
	public void setPosition(int position) {
		positionSlider.setValue(position);
	}
	
	public void setMaxPosition(int maxPosition) {
		positionSlider.setMaximum(maxPosition);
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
		} else if (e.getSource().equals(positionSlider)) {
			for (OptionsObserver o: observerList) {
				int position = ((JSlider)e.getSource()).getValue();
				o.onPositionChanged(position);
			}
		}
	}

	// grouped radio buttons with label
	private JPanel getAnimationTypePanel() {
		JPanel animationTypePanel = new JPanel();
		animationTypePanel.add(new JLabel("Type:"));
		animationType = new ButtonGroup();
		JRadioButton animationTypeTextButton = new JRadioButton("Text");
		animationType.add(animationTypeTextButton);
		animationTypePanel.add(animationTypeTextButton);
		JRadioButton animationTypeAnimationButton = new JRadioButton("Animation");
		animationType.add(animationTypeAnimationButton);
		animationTypePanel.add(animationTypeAnimationButton);
		return animationTypePanel;
	}

	private void initComponents() {
		// one column grid layout
		setLayout(new GridLayout(0,1));
		add(new JLabel("Options"));
		add(getAnimationTypePanel());
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
		add(new JLabel("Frame:"));
		positionSlider = new JSlider(
				SwingConstants.HORIZONTAL, 
				1);
		positionSlider.setPaintTicks(true);
		positionSlider.setSnapToTicks(true);
		positionSlider.setMinorTickSpacing(1);
		positionSlider.addChangeListener(this);
		add(positionSlider);
	}
}
