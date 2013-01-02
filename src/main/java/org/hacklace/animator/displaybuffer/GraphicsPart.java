package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.ConversionUtil.convertAnimationByteTo7Booleans;
import static org.hacklace.animator.ConversionUtil.convertBytesToString;

import java.util.ArrayList;

import org.hacklace.animator.ConversionUtil;
import org.hacklace.animator.configuration.RestOfConfigLine;
import org.hacklace.animator.enums.AnimationType;

public class GraphicsPart extends AnimationPart {

	public GraphicsPart(RestOfConfigLine partialLine) {
		super();
		byte[] aniBytes = ConversionUtil
				.convertAnimationStringToByteArray(partialLine.getDirectMode());
		// cut off $FF in beginning and end
		this.data = new boolean[aniBytes.length][];
		for (int column = 0; column < aniBytes.length; column++) {
			this.data[column] = convertAnimationByteTo7Booleans(aniBytes[column]);
		}

	}

	public GraphicsPart(ArrayList<GraphicByte> graphicByteList) {
		super();
		this.data = new boolean[graphicByteList.size()][];
		for (int i = 0; i < graphicByteList.size(); i++) {
			data[i] = convertAnimationByteTo7Booleans(graphicByteList.get(i)
					.getByte());
		}
	}

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.GRAPHIC;
	}

	public byte[] getColumnsAsBytes() {
		byte[] allByteColumns = new byte[data.length];
		// must avoid trailing $00
		int numberOfUsedColumns = 0;
		for (int colIndex = 0; colIndex < data.length; colIndex++) {
			boolean[] bools = this.data[colIndex];
			assert (bools.length == 7);
			byte value = (byte) ConversionUtil.convertBooleanArrayToByte(bools);
			allByteColumns[colIndex] = value;
			if (value != 0) {
				numberOfUsedColumns = colIndex + 1;
			}
		}
		byte[] usedByteColumns = new byte[numberOfUsedColumns];
		System.arraycopy(allByteColumns, 0, usedByteColumns, 0,
				numberOfUsedColumns);
		return usedByteColumns;
	}

	@Override
	public String toString() {
		return getAnimationType().getDescription();
	}

	public void setColumnRow(int column, int row, boolean value) {
		data[column][row] = value;
	}

	public void toggleColumnRow(int column, int row) {
		data[column][row] = !data[column][row];
	}

	@Override
	public String getRawString() {
		return "$FF " + getRawStringForDirectMode() + "$FF,";
	}

	/**
	 * without beginning and ending $FF,
	 * 
	 * @return
	 */
	public String getRawStringForDirectMode() {
		return convertBytesToString(getColumnsAsBytes());
	}

	@Override
	public boolean isClickEditable() {
		return true;
	}

	@Override
	public int getNumBytes() {
		return data.length;
	}

}
