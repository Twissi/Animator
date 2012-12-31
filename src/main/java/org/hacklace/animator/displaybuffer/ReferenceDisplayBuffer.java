package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.ConversionUtil.convertAnimationByteTo7Booleans;

import org.hacklace.animator.IllegalHacklaceConfigLineException;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.PredefinedAnimation;

public class ReferenceDisplayBuffer extends DisplayBuffer {

	private char letter;
	private PredefinedAnimation animation;

	public ReferenceDisplayBuffer(FullConfigLine fullLine)
			throws IllegalHacklaceConfigLineException {
		super(fullLine.getModusByte());
		setLetter(fullLine.getSixthChar());
	}

	public ReferenceDisplayBuffer(char whichAnimation)
			throws IllegalHacklaceConfigLineException {
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

	/**
	 * side effect: updata data (pixels)
	 * @param letter
	 */
	public void setLetter(char letter) {
		this.letter = letter;
		clearData();
		animation = PredefinedAnimation
				.getPredefinedAnimationByIndex(letter);
		int i = 0;
		for (int aniByte : animation.getAnimationBytes()) {
			boolean[] bits = convertAnimationByteTo7Booleans((byte) aniByte);
			data[i++] = bits;
		}
	}

	@Override
	public String toString() {
		
		return getAnimationType().getDescription() +" "+ animation.toString();
	}

	@Override
	public String getRawStringForRestOfLine() {
		return "~" + this.getLetter();
	}

}
