package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.enums.AnimationType;

public class ReferenceDisplayBuffer extends DisplayBuffer {

	private char letter;

	public ReferenceDisplayBuffer(char letter) {
		this.letter = letter;
	}

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.REFERENCE;
	}

	public char getLetter() {
		return this.letter;
	}

	public void setLetter(char letter) {
		this.letter = letter;
	}

	@Override
	public String toString() {
		return "Animations-Referenz" + " ~" + letter;
	}

}
