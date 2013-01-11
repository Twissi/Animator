package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.ConversionUtil.convertBytesToString;

import org.hacklace.animator.ConversionUtil;
import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.StepWidth;

public class GraphicDisplayBuffer extends DisplayBuffer implements Size {

	public GraphicDisplayBuffer() {
		super();
		data = new boolean[0][];
		setStepWidth(StepWidth.FIVE);
	}

	public GraphicDisplayBuffer(FullConfigLine fullLine,
			ErrorContainer errorContainer) {
		super(fullLine.getModusByte(errorContainer));
		data = fullLine.getRestOfLine(errorContainer).getLeds();
		// may have trailing empty columns (desired!)
	}

	// public void setDataFromBytes(byte[] aniBytes) {
	// for (int column = 0; column < aniBytes.length; column++) {
	// byte aniByte = aniBytes[column];
	// this.data[column] = convertAnimationByteTo7Booleans(aniByte);
	// }
	// }

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.GRAPHIC;
	}

	public byte[] getColumnsAsBytes() {
		byte[] columnsAsBytes = new byte[data.length];

		for (int colIndex = 0; colIndex < data.length; colIndex++) {
			boolean[] bools = this.data[colIndex];
			assert (bools.length == 7);
			byte value = ConversionUtil.convertBooleanArrayToByte(bools);
			columnsAsBytes[colIndex] = value;
		}
		return columnsAsBytes;
	}

	@Override
	public String toString() {
		return getAnimationType().getDescription();
	}

	public void setValueAtColumnRow(int column, int row, boolean value) {
		if (value) {
			increaseWidthIfNeeded(column);
		}
		if (data.length > column) { // do not set columns beyond the end to
									// false
			data[column][row] = value;
		}
	}

	public void toggleColumnRow(int column, int row) {
		increaseWidthIfNeeded(column);
		data[column][row] = !data[column][row];
	}

	private void increaseWidthIfNeeded(int column) {
		if (column >= data.length) {
			boolean[][] oldData = data;
			data = new boolean[column + 1][GRID_ROWS];
			System.arraycopy(oldData, 0, data, 0, oldData.length);
		}
	}

	public void setWidth(int width) {
		assert width >= 0;
		if (width == data.length) {
			// nothing to do
			return;
		}
		if (width > data.length) {
			increaseWidthIfNeeded(width - 1);
			return;
		}
		int highestNonEmptyColumn = getHighestNonEmptyColumn();
		if (width - 1 < highestNonEmptyColumn) {
			// ignore the request as it would cause a data loss
			return;
		}
		// decrease as requested
		boolean[][] oldData = data;
		data = new boolean[width][];
		System.arraycopy(oldData, 0, data, 0, width);
	}

	private int getHighestNonEmptyColumn() {
		int max = -1;
		byte[] columnsAsBytes = getColumnsAsBytes();
		for (int i = 0; i < data.length; i++) {
			if (columnsAsBytes[i] != 0) {
				max = i;
			}
		}
		return max;
	}

	@Override
	public String getRawStringForRestOfLine() {
		return "$FF," + getRawStringForDirectMode() + "$FF,";
	}

	public String getRawStringForDirectMode() {
		return convertBytesToString(getColumnsAsBytes());
	}

	@Override
	public int getNumBytes() {
		return 1 // modus byte
		+ data.length //
		+ 1; // line end
	}

}
