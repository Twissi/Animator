package org.hacklace.animator.gui.actions;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hacklace.animator.gui.BufferReferencer;

public class PositionSliderChangeListener implements ChangeListener {

	private BufferReferencer bufferReferencer;
	private JSlider positionSlider;
	private List<PositionSlideObserver> observers = new LinkedList<PositionSlideObserver>();

	public PositionSliderChangeListener(BufferReferencer bufferReferencer,
			JSlider positionSlider, PositionSlideObserver observer) {
		this.bufferReferencer = bufferReferencer;
		this.positionSlider = positionSlider;
		addPositionSlideObserver(observer);
	}

	public void addPositionSlideObserver(PositionSlideObserver observer) {
		if (observer == null)
			return;
		observers.add(observer);
	}

	@Override
	public void stateChanged(ChangeEvent ce) {
		// ignore the event if we don't have a valid buffer yet
		if (bufferReferencer.getBuffer() == null)
			return;
		for (PositionSlideObserver o : observers) {
			o.onPositionChanged(positionSlider.getValue());
		}
	}

}
