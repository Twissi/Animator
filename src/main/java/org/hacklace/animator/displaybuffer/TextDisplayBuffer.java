package org.hacklace.animator.displaybuffer;


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
	

}
