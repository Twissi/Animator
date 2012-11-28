package org.hacklace.animator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;
import org.hacklace.animator.displaybuffer.ReferenceDisplayBuffer;
import org.hacklace.animator.displaybuffer.TextDisplayBuffer;
import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;

public class HacklaceConfigManager {

	private List<DisplayBuffer> list;

	public HacklaceConfigManager() {
		list = new ArrayList<DisplayBuffer>();
	}

	public void readFile(File file) throws IllegalHacklaceConfigFileException,
			IOException {
		BufferedReader br = null;
		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String cfgLine;
			int lineNumber = 0;
			loop: while ((cfgLine = br.readLine()) != null) {
				lineNumber++;
				if (cfgLine.trim().equals("")) {
					// ignore empty lines (especially at end of file)
					continue loop;
				}
				DisplayBuffer displayBuffer = createBufferFromLine(cfgLine,
						lineNumber, list);
				if (displayBuffer != null /* $00 = EOF */) {
					list.add(displayBuffer);
				} else {
					break loop;
				}
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	/**
	 * 
	 * @param cfgLine
	 * @param lineNumber
	 * @return a DisplayBuffer for the input line, or null for $00, (the last
	 *         line)
	 * @throws IllegalHacklaceConfigFileException
	 */
	private static DisplayBuffer createBufferFromLine(String cfgLine,
			int lineNumber, List<DisplayBuffer> list)
			throws IllegalHacklaceConfigFileException {
		String statusString = cfgLine.substring(0, 3);
		StatusByte statusByte = createStatusByteFromString(statusString,
				lineNumber);
		if (statusByte.isEOF()) {
			return null;
		}
		// text or graphic animation?
		AnimationType animationType = statusByte.getAnimationType();
		DisplayBuffer buffer = null;
		String restOfLine = cfgLine.substring(4);
		if (animationType == AnimationType.TEXT) {
			TextDisplayBuffer textDisplayBuffer = new TextDisplayBuffer();
			String aniText = restOfLine;
			textDisplayBuffer.setText(aniText);
			buffer = textDisplayBuffer;
		} else if (restOfLine.startsWith("$FF")) {
			GraphicDisplayBuffer graphicDisplayBuffer = new GraphicDisplayBuffer();
			byte[] aniBytes = createByteArrayFromString(
					restOfLine.substring(4, restOfLine.length() - 4),
					lineNumber); // cut off $FF in beginning and end
			graphicDisplayBuffer.setDataFromBytes(aniBytes);
			buffer = graphicDisplayBuffer;
		} else if (restOfLine.startsWith("~")) {
			char letter = restOfLine.charAt(1);

			ReferenceDisplayBuffer referenceDisplayBuffer = new ReferenceDisplayBuffer(
					letter, list);
			buffer = referenceDisplayBuffer;
		} else {
			throw new IllegalHacklaceConfigFileException(
					"Illegal hacklace configuration file, error in line "
							+ lineNumber
							+ ", modus is graphic, but neither ~ nor $FF is sent at beginning.");
		}
		assert (buffer != null);
		buffer.setDirection(statusByte.getDirection());
		buffer.setSpeed(statusByte.getSpeed());
		buffer.setDelay(statusByte.getDelay());
		return buffer;
	}

	private static byte[] createByteArrayFromString(String aniString,
			int lineNumber) throws IllegalHacklaceConfigFileException {
		final String separators = "[ ,;./:_+*|]";
		byte[] aniBytes = new byte[200];
		String[] aniByteStrings = aniString.split(separators);
		if (aniBytes.length > 200) {
			throw new IllegalHacklaceConfigFileException(
					"Illegal hacklace configuration file: More than 200 bytes in line "
							+ lineNumber + ".");

		}
		int index = 0;

		for (String aniByteString : aniByteStrings) {

			byte aniByte = convertStringToByte(aniByteString);
			aniBytes[index] = aniByte;
			index++;

		}
		return aniBytes;

	}

	private static StatusByte createStatusByteFromString(String statusString,
			int line) throws IllegalHacklaceConfigFileException {
		if (!isHexSequence(statusString)) {
			throw new IllegalHacklaceConfigFileException("Status string "
					+ statusString + " is not hex ($nn) in line " + line + ".");
		}
		byte statusByte_ = convertStringToByte(statusString);
		StatusByte statusByte = new StatusByte(statusByte_);
		return statusByte;
	}

	private static byte convertStringToByte(String statusString) {
		return (byte) Integer.parseInt(statusString.substring(1), 16);
	}

	public void writeFile(File file) throws IOException {
		BufferedWriter out = null;
		try {
			FileWriter fw = new FileWriter(file);
			out = new BufferedWriter(fw);

			for (DisplayBuffer displayBuffer : this.list) {
				Direction direction = displayBuffer.getDirection();
				Delay delay = displayBuffer.getDelay();
				AnimationType animationType = displayBuffer.getAnimationType();
				Speed speed = displayBuffer.getSpeed();
				boolean isReferenceDisplayBuffer = displayBuffer.isReferenceBuffer();
				StatusByte statusByte = new StatusByte(direction, delay,
						animationType, speed);
				StringBuilder stringBuilder = new StringBuilder();
				String statusByteString = convertByteToString(statusByte
						.getByte());
				stringBuilder.append(statusByteString).append(",");
				if (animationType == AnimationType.TEXT) {
					TextDisplayBuffer textDisplayBuffer = (TextDisplayBuffer) displayBuffer;
					stringBuilder.append(textDisplayBuffer.getText());
				} else if (animationType == AnimationType.GRAPHIC
						&& !isReferenceDisplayBuffer) {
					GraphicDisplayBuffer graphicDisplayBuffer = (GraphicDisplayBuffer) displayBuffer;
					byte[] columns = graphicDisplayBuffer.getColumnsAsBytes();
					stringBuilder.append("$FF ");
					for (byte column : columns) {
						stringBuilder.append(convertByteToString(column))
								.append(" ");
					}
					stringBuilder.append("$FF,");
				} else {
					assert (isReferenceDisplayBuffer);
					ReferenceDisplayBuffer referenceDisplayBuffer = (ReferenceDisplayBuffer) displayBuffer;
					stringBuilder.append("~").append(
							referenceDisplayBuffer.getLetter());
				}
				stringBuilder.append("\n");
				out.write(stringBuilder.toString());
			}
			out.write("$00,");
		} finally {
			if (out != null)
				out.close();
		}
	}

	private static String convertByteToString(byte number) {
		int value = number;
		if (value < 0)
			value += 256;
		String leadingZero = (value < 0x10) ? "0" : "";
		return "$" + leadingZero + Integer.toString(value, 16).toUpperCase();
	}

	private void addDisplayBuffer(DisplayBuffer buffer) {
		list.add(buffer);
	}

	public void addTextDisplayBuffer() {
		TextDisplayBuffer tdb = new TextDisplayBuffer();
		addDisplayBuffer(tdb);
	}

	public void addGraphicDisplayBuffer() {
		GraphicDisplayBuffer gdb = new GraphicDisplayBuffer();
		addDisplayBuffer(gdb);
	}

	public void deleteDisplayBuffer(int index) {
		list.remove(index);
	}

	public void moveUp(int index) {
		if (index == 0) {
			// do not throw an error, just do nothing
			return;
		}
		swap(index, index - 1);
	}

	public void moveDown(int index) {
		if (index == list.size() - 1) {
			// if index is already last, do nothing (no error)
			return;
		}
		swap(index, index + 1);
	}

	private void swap(int index1, int index2) {
		DisplayBuffer buffer1 = list.get(index1);
		DisplayBuffer buffer2 = list.get(index2);
		list.set(index1, buffer2);
		list.set(index2, buffer1);
	}

	private static boolean isHexSequence(String potentialHexSequence) {
		return potentialHexSequence.matches("^\\$[0-9A-F]{2}$"); // $nn (exactly
																	// 3 chars)
	}

	public List<DisplayBuffer> getList() {
		return list;
	}

	public DisplayBuffer getDisplayBuffer(int index) {
		return list.get(index);
	}

}
