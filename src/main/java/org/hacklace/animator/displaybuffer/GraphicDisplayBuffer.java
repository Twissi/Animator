package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.ConversionUtil.convertAnimationByteTo7Booleans;
import static org.hacklace.animator.ConversionUtil.convertBytesToString;

import org.hacklace.animator.ConversionUtil;
import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.enums.AnimationType;

public class GraphicDisplayBuffer extends DisplayBuffer implements Size {

	public GraphicDisplayBuffer() {
		super();
	}

	public GraphicDisplayBuffer(FullConfigLine fullLine, ErrorContainer errorContainer) {
		super(fullLine.getModusByte(errorContainer));
		byte[] aniBytes = ConversionUtil
				.convertAnimationStringToByteArray(fullLine.getRestOfLine()
						.getDirectMode(), errorContainer);
		// cut off $FF in beginning and end
		this.setDataFromBytes(aniBytes);

	}

	public void setDataFromBytes(byte[] aniBytes) {
		for (int column = 0; column < aniBytes.length; column++) {
			byte aniByte = aniBytes[column];
			this.data[column] = convertAnimationByteTo7Booleans(aniByte);
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
			byte value = ConversionUtil.convertBooleanArrayToByte(bools);
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
	public String getRawStringForRestOfLine() {
		return "$FF " + getRawStringForDirectMode() + "$FF,";
	}

	public String getRawStringForDirectMode() {
		return convertBytesToString(getColumnsAsBytes());
	}

	@Override
	public int getNumBytes() {
		return 1 // modus byte
		+ countUsedColumns() //
		+ 1; // line end
	}
}
