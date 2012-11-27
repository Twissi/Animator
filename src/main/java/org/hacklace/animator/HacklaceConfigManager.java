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

	private final static String HEX_PATTERN = "^\\$[0-9A-F]{2}$";
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
			String text = cfgLine.substring(4);
			textDisplayBuffer.setText(text);
			buffer = textDisplayBuffer;
		} else {
			GraphicDisplayBuffer graphicDisplayBuffer = new GraphicDisplayBuffer();
			Scanner scanner = new Scanner(cfgLine.substring(4));
			byte[] aniBytes = new byte[200];
			int index = 0;
			while (scanner.hasNext(HEX_PATTERN)) {
				if (index >= 200)
					throw new IllegalHacklaceConfigFileException(
							"Illegal hacklace configuration file: More than 200 bytes in line "
									+ lineNumber + ".");
				String aniByteString = scanner.next(HEX_PATTERN);
				byte aniByte = convertStringToByte(aniByteString);
				aniBytes[index] = aniByte;
				index++;

			}
			scanner.close();
			graphicDisplayBuffer.setDataFromBytes(aniBytes);
			buffer = graphicDisplayBuffer;
		}
		buffer.setDirection(statusByte.getDirection());
		buffer.setSpeed(statusByte.getSpeed());
		buffer.setDelay(statusByte.getDelay());
		return buffer;
	}

	private static StatusByte createStatusByteFromString(String statusString,
			int line) throws IllegalHacklaceConfigFileException {
		if (!isHexSequence(statusString)) {
			throw new IllegalHacklaceConfigFileException("Status string "
					+ statusString + " is not hex ($xx) in line " + line + ".");
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
		return potentialHexSequence.matches(HEX_PATTERN); // $nn
	}

}
