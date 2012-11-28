package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.enums.AnimationType;


public class TextDisplayBuffer extends DisplayBuffer{
	
	private String text;

	@Override
	public int getStepWidth() {
		return 1;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.TEXT;
	}
	

}
