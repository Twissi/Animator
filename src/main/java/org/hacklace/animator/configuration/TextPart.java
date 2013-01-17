package org.hacklace.animator.configuration;

import static org.hacklace.animator.ConversionUtil.convertAnimationByteTo7Booleans;

import java.util.List;

import org.hacklace.animator.ConversionUtil;
import org.hacklace.animator.enums.AnimationType;

public class TextPart extends AnimationPart  {

	private String rawString = "";
	
	private int numBytes;
	
	public TextPart(List<TextElement> textElementList) {
		super();
		int numColumns = 0;
		for (TextElement textElement : textElementList) {
			numColumns += textElement.getNumColumns();
			rawString += textElement.getRawString();
		}

		this.data = new boolean[numColumns][];

		int totalColumn = 0;
		for (TextElement textElement : textElementList) {
			int[] animationBytes = textElement.getAnimationBytes();

			for (int aniByte : animationBytes) {
				boolean[] bits = convertAnimationByteTo7Booleans((byte) aniByte);
				data[totalColumn++] = bits;
			}
			
			numBytes += textElement.getNumBytes();
		}
	}

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.TEXT;
	}

	@Override
	public String toString() {
		return getAnimationType().getDescription() + " " + rawString;
	}

	@Override
	public boolean isClickEditable() {
		return false;
	}

	@Override
	public String getRawString() {
		return rawString;
	}

	@Override
	public int getNumBytes() {
		return numBytes;
	}

	/**
	 * must only be called for TextParts at the end of a line
	 */
	public void removeExtraColumnFromEnd() {
		if (data.length == 0) {
			// happens for completely empty buffer
			return;
		}
		assert (ConversionUtil.isEmptyColumn(data[data.length - 1]));
		boolean[][] oldData = data;
		data = new boolean[oldData.length-1][];
		System.arraycopy(oldData, 0, data, 0, data.length);
	}


}
