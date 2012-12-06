package org.hacklace.animator.displaybuffer;

import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.gui.FontUtil;

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
		for (char c : text.toCharArray()) {
			FontUtil.getFiveBytesForChar(c); // TODO byte to boolean array, set data	
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
