package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.ConversionUtil.convertAnimationByteTo7Booleans;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.PredefinedAnimation;

public class ReferenceDisplayBuffer extends DisplayBuffer implements Size {

	private char letter;
	private PredefinedAnimation animation;

	public ReferenceDisplayBuffer(FullConfigLine fullLine,
			ErrorContainer errorContainer) {
		super(fullLine.getModusByte(errorContainer));
		char c = '?';
		String twoChars = fullLine.getRestOfLine().getValue();
		if (twoChars.length() >= 2) {
			c = twoChars.charAt(1);
		}
		if (twoChars.length() != 2) {
			errorContainer.addError("Reference animation (" + twoChars
					+ ") needs to have length 2 (~X). (Using ~? now.)");
		} else if (!twoChars.startsWith("~")) {
			errorContainer.addError("Reference animation (" + twoChars
					+ ") needs to start with ~. (Using ~" + c + " now.)");
		}
		setLetter(c, errorContainer);
	}

	public ReferenceDisplayBuffer(char whichAnimation,
			ErrorContainer errorContainer) {
		super();

		// default modus byte is already set
		setLetter(whichAnimation, errorContainer);
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
	public void setLetter(char letter, ErrorContainer errorContainer) {
		this.letter = letter;
		if (PredefinedAnimation.isInvalid(letter)) {
			errorContainer.addError("Reference animation (~" + letter
					+ ") is not valid. Needs to be upper case between ~"
					+ PredefinedAnimation.MIN + " and "
					+ PredefinedAnimation.MAX + ".");
		}

		clearData();
		animation = PredefinedAnimation.getPredefinedAnimationByIndex(letter);
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
	public String getRawStringForRestOfLine() {
		return "~" + this.getLetter();
	}

	@Override
	public int getNumBytes() {
		return 4;
	}

}
