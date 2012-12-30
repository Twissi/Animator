package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.ConversionUtil.convertAnimationByteTo7Booleans;

import org.hacklace.animator.ModusByte;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.PredefinedAnimation;

public class ReferenceDisplayBuffer extends DisplayBuffer {

	private char letter;

	public ReferenceDisplayBuffer(ModusByte modusByte, char letter) {
		super();
		this.modusByte = modusByte;
		setLetter(letter);
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
		clearData();
		for (PredefinedAnimation animation: PredefinedAnimation.values()) {
			if (animation.getIndex() == letter) {
				int i = 0;
				for (int aniByte : animation.getAnimationBytes()) {
					boolean[] bits = convertAnimationByteTo7Booleans((byte)aniByte);
					data[i++] = bits;
				}
			}
		}
	}

	@Override
	public String toString() {
		return getAnimationType().getDescription() + " ~" + letter;
	}
	
	@Override
	public String getRawString() {
		return this.modusByte.getRawString() + "~" + this.getLetter();
	}

}
