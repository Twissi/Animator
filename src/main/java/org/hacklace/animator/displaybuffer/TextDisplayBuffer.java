package org.hacklace.animator.displaybuffer;

import java.util.Arrays;

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
	
	private boolean isEscape(char c) {
		return (c == '^' || c == '~' || c == '$');
	}
	
	/**
	 * side effect: update data (bits/bytes)
	 * @param text
	 */
	public void setText(String text) {
		clearData();
		this.text = text;
		int col = 0;
		for (int i = 0; i < text.length(); i++) {
			int[] animationBytes;
			char c = text.charAt(i);
			if (isEscape(c)) {
				// handle escape characters
				i++;
				// ignore escape character at the end of the string
				if (i > text.length() - 1) return;
				char next = text.charAt(i);
				// escape for the character itself?
				if (next == c) {
					animationBytes = FontUtil.getMinimumBytesForChar(c);
				} else {
					switch (c) {
					case '^':
						animationBytes = FontUtil.getMinimumBytesForSpecial(next);
						break;
					default:
						// TODO A reference to an animation in a text animation... What do?
						// For a non-fatal fallback, display the escape sequence instead...
						i--;
						c = text.charAt(i);
						animationBytes = FontUtil.getMinimumBytesForChar(c);
					}
					
				}
			} else {
				// normal character
				animationBytes = FontUtil.getMinimumBytesForChar(c);
			}
			for (int byteNum = 0; byteNum < DisplayBuffer.COLUMNS; byteNum++) {
				for (int bit = 0; bit < 7; bit++) {
					if (byteNum < animationBytes.length) {
						try {
							data[byteNum + DisplayBuffer.COLUMNS * col][bit] = (animationBytes[byteNum] & (int)Math.pow(2, bit)) != 0;
						} catch (ArrayIndexOutOfBoundsException ex) {
							System.err.println("Error creating text buffer. col=" + col + " i=" + i + " c=" + c + " byteNum=" + byteNum);
						}
					} else {
						// clear all bits after the last column from the font
						data[byteNum + DisplayBuffer.COLUMNS * col][bit] = false;
					}
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
