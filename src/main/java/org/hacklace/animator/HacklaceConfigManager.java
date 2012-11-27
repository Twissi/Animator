package org.hacklace.animator;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;
import org.hacklace.animator.displaybuffer.TextDisplayBuffer;
import org.hacklace.animator.enums.AnimationType;

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
			while ((cfgLine = br.readLine()) != null) {
				lineNumber++;
				DisplayBuffer displayBuffer = createBufferFromLine(cfgLine,
						lineNumber);
				if (displayBuffer != null /* $FF = EOF */) {
					list.add(displayBuffer);
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
	 * @return a DisplayBuffer for the inputline, or null for $00, (the last
	 *         line)
	 * @throws IllegalHacklaceConfigFileException
	 */
	private static DisplayBuffer createBufferFromLine(String cfgLine,
			int lineNumber) throws IllegalHacklaceConfigFileException {
		String statusString = cfgLine.substring(0, 3);
		StatusByte statusByte = createStatusByteFromString(statusString,
				lineNumber);
		if (statusByte.isEOF()) {
			return null;
		}
		// text or graphic animation?
		AnimationType animationType = statusByte.getAnimationType();
		DisplayBuffer buffer;
		if (animationType == AnimationType.TEXT) {
			TextDisplayBuffer textDisplayBuffer = new TextDisplayBuffer();
			String aniText = cfgLine.substring(4);
			textDisplayBuffer.setText(aniText);
			buffer = textDisplayBuffer;
		} else {
			GraphicDisplayBuffer graphicDisplayBuffer = new GraphicDisplayBuffer();
			byte[] aniBytes = createByteArrayFromString(cfgLine.substring(4),
					lineNumber);
			graphicDisplayBuffer.setDataFromBytes(aniBytes);
			buffer = graphicDisplayBuffer;
		}
		buffer.setDirection(statusByte.getDirection());
		buffer.setSpeed(statusByte.getSpeed());
		buffer.setDelay(statusByte.getDelay());
		return buffer;
	}

	private static byte[] createByteArrayFromString(String aniString,
			int lineNumber) throws IllegalHacklaceConfigFileException {
		final String separators = "[ ,;./:_+*]";
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

	public void writeFile(File file) {

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
