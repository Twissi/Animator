package org.hacklace.animator.gui;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Speed;

public class AnimationOptionsPanel extends JPanel {
	private static final long serialVersionUID = -2625306373507959134L;
	
	public AnimationOptionsPanel() {
		removeAll();
		reset();
	}

	// grouped radio buttons with label
	private JPanel getAnimationTypePanel() {
		JPanel animationTypePanel = new JPanel();
		animationTypePanel.add(new JLabel("Type:"));
		ButtonGroup animationType = new ButtonGroup();
		JRadioButton animationTypeTextButton = new JRadioButton("Text");
		animationType.add(animationTypeTextButton);
		animationTypePanel.add(animationTypeTextButton);
		JRadioButton animationTypeAnimationButton = new JRadioButton("Animation");
		animationType.add(animationTypeAnimationButton);
		animationTypePanel.add(animationTypeAnimationButton);
		return animationTypePanel;
	}

	public void reset() {
		// one column grid layout
		setLayout(new GridLayout(0,1));
		add(new JLabel("Options"));
		add(getAnimationTypePanel());
		add(new JLabel("Speed:"));
		JSlider speedSlider = new JSlider(
				JSlider.HORIZONTAL, 
				Speed.values().length - 1
				);
		speedSlider.setPaintTicks(true);
		speedSlider.setSnapToTicks(true);
		speedSlider.setMinorTickSpacing(1);
		add(speedSlider);
		add(new JLabel("Delay:"));
		JSlider delaySlider = new JSlider(
				JSlider.HORIZONTAL, 
				Delay.values().length - 1);
		delaySlider.setPaintTicks(true);
		delaySlider.setSnapToTicks(true);
		delaySlider.setMinorTickSpacing(1);
		add(delaySlider);
	}
}
