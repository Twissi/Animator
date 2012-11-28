package org.hacklace.animator.displaybuffer;

import java.util.List;

import org.hacklace.animator.enums.AnimationType;

public class ReferenceDisplayBuffer extends DisplayBuffer {

	private char letter;
	private List<DisplayBuffer> list;

	public ReferenceDisplayBuffer(char letter, List<DisplayBuffer> list) {
		this.letter = letter;
		this.list = list;
	}
	
	private DisplayBuffer getReferencedDisplayBuffer() {
	int index = letter - 'A';
	DisplayBuffer otherDisplayBuffer = this.list.get(index);
	return otherDisplayBuffer;
	}

	@Override
	public int getStepWidth() { return
		getReferencedDisplayBuffer().getStepWidth();
	}

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.GRAPHIC;
	}
	
	@Override
	public boolean isReference() {
		return true;
	}

	public char getLetter() {
		return this.letter;
	}
	
}
