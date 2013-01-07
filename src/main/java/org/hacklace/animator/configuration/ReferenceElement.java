package org.hacklace.animator.configuration;

import static org.hacklace.animator.ConversionUtil.convertAnimationByteTo7Booleans;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.displaybuffer.Size;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.PredefinedAnimation;

public class ReferenceElement extends AnimationPart implements Size {
	private char letter;
	private PredefinedAnimation animation;

	/**
	 * 
	 * @param whichAnimation should be a valid animation (selection restricted in UI)
	 */
	public ReferenceElement(char whichAnimation) {
		super();
		ErrorContainer e = new ErrorContainer();
		assert(PredefinedAnimation.isValid(whichAnimation, e));
		// default modus byte is already set
		setLetter(whichAnimation, e);
		assert(e.isEmpty());
	}

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.REFERENCE;
	}

	public char getLetter() {
		return this.letter;
	}

	/**
	 * side effect: updata data (pixels)
	 * 
	 * @param letter
	 */
	private void setLetter(char letter, ErrorContainer errorContainer) {
		this.letter = letter;
		animation = PredefinedAnimation.getPredefinedAnimationByIndex(letter, errorContainer);
		data = new boolean[animation.getAnimationBytes().length][];
		int i = 0;
		for (int aniByte : animation.getAnimationBytes()) {
			boolean[] bits = convertAnimationByteTo7Booleans((byte) aniByte);
			data[i++] = bits;
		}
	}

	@Override
	public String toString() {

		return getAnimationType().getDescription() + " " + animation.toString();
	}

	@Override
	public String getRawString() {
		return "~" + this.getLetter();
	}

	@Override
	public boolean isClickEditable() {
		return false;
	}

	@Override
	public int getNumBytes() {
		return 2;
	}
}
