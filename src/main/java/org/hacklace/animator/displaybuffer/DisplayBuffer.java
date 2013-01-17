package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.ConversionUtil.isEmptyColumn;

import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.ModusByte;
import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;
import org.hacklace.animator.enums.StepWidth;
import org.hacklace.animator.gui.AnimatorGui;

public abstract class DisplayBuffer implements Cloneable {

	protected boolean[][] data;

	protected ModusByte modusByte = new ModusByte();

	protected final static int GRID_ROWS = AnimatorGui.getIniConf().rows();
	protected final static int GRID_COLS = AnimatorGui.getIniConf().columns(); // only
																				// for
																				// copying
																				// frames

	protected DisplayBuffer() {
	}

	protected DisplayBuffer(ModusByte modusByte) {
		this.modusByte = modusByte;
	}

	public StepWidth getStepWidth() {
		return modusByte.getStepWidth();
	}

	public Direction getDirection() {
		return this.modusByte.getDirection();
	}

	public Speed getSpeed() {
		return this.modusByte.getSpeed();
	}

	public Delay getDelay() {
		return this.modusByte.getDelay();
	}

	public void setDirection(Direction direction) {
		this.modusByte.setDirection(direction);
	}

	public void setDelay(Delay delay) {
		this.modusByte.setDelay(delay);
	}

	public void setStepWidth(StepWidth stepWidth) {
		this.modusByte.setStepWidth(stepWidth);
	}

	public void setSpeed(Speed speed) {
		this.modusByte.setSpeed(speed);
	}

	public ModusByte getModusByte() {
		return modusByte;
	}

	public abstract AnimationType getAnimationType();

	@Override
	public String toString() {
		return "DisplayBuffer";
	}

	@Override
	public DisplayBuffer clone() {
		try {
			DisplayBuffer copy = (DisplayBuffer) super.clone();
			copy.modusByte = this.modusByte.clone();
			copy.data = new boolean[this.data.length][GRID_ROWS];
			for (int colIndex = 0; colIndex < this.data.length; colIndex++) {
				boolean[] column = this.data[colIndex];
				copy.data[colIndex] = column.clone();
			}
			return copy;
		} catch (CloneNotSupportedException e) {
			return null;
		}

	}

	public boolean getValueAtColumnRow(int column, int row) {
		if (column > data.length - 1) {
			return false;
		}
		return data[column][row];
	}

	/**
	 * 
	 * @param cfgLine
	 * @return a DisplayBuffer for the input line, or null for $00, (the last
	 *         line)
	 */
	public static DisplayBuffer createBufferFromLine(FullConfigLine fullLine,
			ErrorContainer errorContainer) {
		ModusByte modusByte = fullLine.getModusByte(errorContainer);
		if (modusByte.isEOF()) {
			return null;
		}

		AnimationType animationType = fullLine.getRestOfLine(errorContainer)
				.analyzeType();

		switch (animationType) {
		case TEXT:
			return new TextDisplayBuffer(fullLine, errorContainer);
		case GRAPHIC:
			return new GraphicDisplayBuffer(fullLine, errorContainer);
		case REFERENCE:
			return new ReferenceDisplayBuffer(fullLine, errorContainer);
		case MIXED:
			return new MixedDisplayBuffer(fullLine, errorContainer);
		}
		return null;
	}

	/**
	 * Generates the raw string used in config files for the whole line
	 * including modus byte
	 * 
	 * @return the raw string used in config files
	 */
	public final FullConfigLine getFullConfigLine() {
		return new FullConfigLine(modusByte.getRawString()
				+ getRawStringForRestOfLine());
	}

	/**
	 * without modus byte
	 * 
	 * @return
	 */
	public abstract String getRawStringForRestOfLine();

	/**
	 * under certain circumstances the buffer cannot be saved. modusByte 0 means
	 * "EOF" so this can't be used.
	 * 
	 * @return
	 */
	public boolean isSaveable(ErrorContainer errorContainer) {
		if (modusByte.getByte() == 0) {
			errorContainer.addError(ZERO_MODUS_BYTE);
		}
		getFullConfigLine().getRestOfLine(errorContainer);

		additionalChecks(errorContainer);

		return errorContainer.isErrorFree();
	}

	public final static String ZERO_MODUS_BYTE = "You cannot combine speed 0, delay 0, unidirectional, step width 1 as this would result in the illegal modus byte 0.";

	/**
	 * for the UI, the number of LED columns. Maximum 200 per animation.
	 */
	public int getNumColumns() {
		return data.length;
	}

	/**
	 * number of bytes, e.g. 2 for a reference, 1 per letter, 1 per byte in
	 * direct mode. Add 1 for modus byte, 1 for 0 delimiter at end of line.
	 * Maximum 256 across all animations.
	 */
	public abstract int getNumBytes();

	public void additionalChecks(ErrorContainer errorContainer) {
		// recommend four empty columns in the beginning (only step width ONE)
		if (getStepWidth() == StepWidth.ONE
				&& (data.length < 4 || !isEmptyColumn(data[0])
						|| !isEmptyColumn(data[1]) || !isEmptyColumn(data[2]) || !isEmptyColumn(data[3]))) {
			// does not begin with four empty columns
			if (getAnimationType() == AnimationType.GRAPHIC) {
				errorContainer
						.addInformationMessage("You may want to begin with four empty columns, otherwise the animation will look abrupt.");
			} else { // text, mixed, reference->mixed
				errorContainer
						.addInformationMessage("You may want to begin with a space, otherwise the animation will look abrupt.");
			}
		} // end recommend four empty columns in the beginning

		int length = data.length;

		// recommend five empty columns in the end
		// (only if delay over 0 or step width ONE or both)
		if ((getDelay() != Delay.ZERO || getStepWidth() == StepWidth.ONE)
				&& (length < 5 || !isEmptyColumn(data[length - 1])
						|| !isEmptyColumn(data[length - 2])
						|| !isEmptyColumn(data[length - 3])
						|| !isEmptyColumn(data[length - 4]) || !isEmptyColumn(data[length - 5]))) {
			// does not end in five empty columns
			if (getAnimationType() == AnimationType.GRAPHIC) {
				errorContainer
						.addInformationMessage("You may want to end with five empty columns.");
			} else { // text, mixed, reference->mixed
				errorContainer
						.addInformationMessage("You may want to end with $9D, (wide space / five empty columns).");
			}
		}

		// warn if not divisible by 5
		// (only if step width 5)
		final int COLS = AnimatorGui.getIniConf().columns(); // 5
		int rest = length % COLS;
		if (getStepWidth() == StepWidth.FIVE && rest != 0) {
			if (length < COLS) {
				errorContainer.addWarning("You need at least " + COLS
						+ " columns, otherwise nothing will be displayed.");
				return;
			}
			int previousFrameStart = length - rest - COLS;
			boolean isLastFullFrameAndRestEmpty = true;
			// columns
			for (int i = previousFrameStart; i < length; i++) {
				isLastFullFrameAndRestEmpty &= isEmptyColumn(data[i]);
			}
			boolean isRestEmpty = true;
			for (int i = previousFrameStart + COLS; i < length; i++) {
				isRestEmpty &= isEmptyColumn(data[i]);
			}

			int antiRest = COLS - rest;

			if (isLastFullFrameAndRestEmpty) {
				if (getAnimationType() == AnimationType.GRAPHIC) {
					errorContainer
							.addInformationMessage("The number of columns is not divisible by "
									+ COLS
									+ ", you can delete the last "
									+ rest + " empty column(s) ($00,) .");
				} else {
					// in the case of text (or potential text in mixed buffers)
					// say nothing because a final space or $9D, often does not
					// result in divisibility by 5 and that's fine
				}
			} else { // !isLastFullFrameAndRestEmpty
				if (getAnimationType() == AnimationType.GRAPHIC) {
					if (!isRestEmpty) {
						errorContainer
								.addError("The number of columns is not divisible by "
										+ COLS
										+ ", you must add "
										+ antiRest
										+ " empty column(s) ($00,).");
						errorContainer
								.addError("Otherwise the last frame will not be displayed.");
					} else { // isRestEmpty
						errorContainer
								.addInformationMessage("The number of columns is not divisible by "
										+ COLS
										+ ", you may want to add "
										+ antiRest + " empty column(s) ($00,).");
						errorContainer
								.addInformationMessage("Otherwise the final empty frame will not be displayed.");
					}

				} else { // Text, Reference, Mixed
					if (!isRestEmpty) {
						errorContainer
								.addError("The number of columns is not divisible by "
										+ COLS
										+ ", you must add a space (to get at least "
										+ antiRest + " more empty column(s)).");
						errorContainer
								.addError("Otherwise the last frame will not be displayed.");
					} else { // isRestEmpty
						errorContainer
								.addInformationMessage("The number of columns is not divisible by "
										+ COLS
										+ ", you may want to add a space (to get at least "
										+ antiRest + " more empty column(s)).");
						errorContainer
								.addInformationMessage("Otherwise the final empty frame will not be displayed.");
					}
				}

			}

		}
	}
}
