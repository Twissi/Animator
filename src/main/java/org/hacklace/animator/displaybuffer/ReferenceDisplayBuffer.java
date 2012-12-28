package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.ModusByte;
import org.hacklace.animator.enums.AnimationType;

public class ReferenceDisplayBuffer extends DisplayBuffer {

	private char letter;

	public ReferenceDisplayBuffer(ModusByte modusByte, char letter) {
		super();
		this.modusByte = modusByte;
		this.letter = letter;
	}

	public ReferenceDisplayBuffer(char whichAnimation) {
		super();
		this.letter = whichAnimation;
		// default modus byte is already set
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
		return getAnimationType().getDescription() + " ~" + letter;
	}

}
