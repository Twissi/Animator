package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.enums.AnimationType;

public class TextDisplayBuffer extends DisplayBuffer {

	private String text;

	public TextDisplayBuffer() {
		super();
		this.text = "";
	}

	public TextDisplayBuffer(String text) {
		super();
		setText(text);
			}

	public String getText() {
		return text;
	}

	/**
	 * side effect: update data (bits/bytes)
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
		int col = 0;
		for (char c : text.toCharArray()) {
			int[] animationBytes = FontUtil.getMinimumBytesForChar(c);
			for (int i = 0; i < animationBytes.length; i++) {
				for (int bit = 0; bit < 7; bit++) {
					data[i + DisplayBuffer.COLUMNS * col][bit] = (animationBytes[i] & (int)Math.pow(2, bit)) != 0;
				}
			}
			col++;
		}

	}

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.TEXT;
	}

	@Override
	public String toString() {
		return "Text-Animation" + " " + text;
	}

}
