package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.ConversionUtil.convertAnimationByteTo7Booleans;
import static org.hacklace.animator.ConversionUtil.convertStringToInt;
import static org.hacklace.animator.ConversionUtil.isHexSequence;

import org.hacklace.animator.ModusByte;
import org.hacklace.animator.enums.AnimationType;

public class TextDisplayBuffer extends DisplayBuffer {

	private String text;

	public TextDisplayBuffer() {
		super();
		this.text = "";
	}

	public TextDisplayBuffer(ModusByte modusByte, String text) {
		super();
		this.modusByte = modusByte;
		setText(text);
	}

	public String getText() {
		return text;
	}

	/**
	 * side effect: update data (bits/bytes)
	 * 
	 * @param text
	 */
	public void setText(String text) {
		clearData();
		this.text = text;

		int totalAnimationByteIndex = 0;
		loopOverText : for (int i = 0; i < text.length(); i++) {
			int[] oneToFiveAnimationBytes = new int[0]; // one to five bytes
			char c = text.charAt(i);

			// normal characters
			if (c != '~' && c != '^' && c != '$') {
				oneToFiveAnimationBytes = FontUtil.getMinimumBytesForChar(c);
			}
			// escape characters ~, &, $
			else {
				i++; // first
				// ignore escape character at the end of the string
				if (i > text.length() - 1)
					break loopOverText;
				char next = text.charAt(i);
				// escape for the character itself? I.e. ~~ ^^ $$
				if (next == c) { // Yes, ~~ ^^ $$
					oneToFiveAnimationBytes = FontUtil
							.getMinimumBytesForChar(c);
				} else { // No, special chars or reference animation
					switch (c) {
						case '^' : // (but not ^^) ^A for € etc.
							oneToFiveAnimationBytes = FontUtil
									.getMinimumBytesForSpecial(next);
							break;
						case '$' : // (but not $$) $80 for € etc.
							String charSetIndexAsThreeCharString = "$" + next;
							i++; // second
							// ignore if end of string
							if (i > text.length() - 1)
								break loopOverText;
							charSetIndexAsThreeCharString += text.charAt(i);
							if (isHexSequence(charSetIndexAsThreeCharString)) {
								int charSetIndex = convertStringToInt(charSetIndexAsThreeCharString);
								oneToFiveAnimationBytes = FontUtil
										.getMinimumBytesForIndex(charSetIndex);
								i++; // third: there are actually four chars: $nn, i.e. one separator
								     // (separator is comma in default config, but can be space and others)
							} else {
								// probably the user is in the process of typing
								i--; // undo second
								i--; // undo first
								// temporarily just display the $ until the user
								// has finished typing
								oneToFiveAnimationBytes = FontUtil
										.getMinimumBytesForChar('$');
							}
							break;
						case '~' :
							// TODO A reference to an animation in a text
							// animation... What do?
							// For a non-fatal fallback, display the escape
							// sequence
							// instead...
							i--;
							oneToFiveAnimationBytes = FontUtil
									.getMinimumBytesForChar(c);
							break;
					}
				}
			}

			for (int aniByte : oneToFiveAnimationBytes) {
				boolean[] bits = convertAnimationByteTo7Booleans((byte) aniByte);
				data[totalAnimationByteIndex] = bits;
				totalAnimationByteIndex++;
			} // end loop over one to five animation bytes

		} // end loop over text

		// clear all bits after the last column
		for (int column = totalAnimationByteIndex; column < MAX_COLUMNS; column++) {
			for (int row = 0; row < gridRows /* 7 */; row++) {
				data[column][row] = false;
			}
		}
	} // end method

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.TEXT;
	}

	@Override
	public String toString() {
		return getAnimationType().getDescription() + " " + text;
	}

	@Override
	public String getRawString() {
		return modusByte.getRawString() + getText();
	}

}
