package org.hacklace.animator.configuration;

import static org.hacklace.animator.ConversionUtil.convertAnimationByteTo7Booleans;

import java.util.List;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.displaybuffer.FontUtil;
import org.hacklace.animator.displaybuffer.Size;
import org.hacklace.animator.enums.AnimationType;

public class TextPart extends AnimationPart implements Size {

	private String rawString = "";
	
	public TextPart(String rawString, ErrorContainer errorContainer) {
		this.rawString = rawString;

		byte[] animationBytes = FontUtil.getBytesForRawString(rawString, errorContainer);
		this.data = new boolean[animationBytes.length][];

		int i = 0;
		for (byte aniByte : animationBytes) {
			boolean[] bits = convertAnimationByteTo7Booleans(aniByte);
			data[i++] = bits;
		}
	}

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
		return data.length;
	}


}
