package org.hacklace.animator;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.hacklace.animator.configuration.FullConfigLine;
import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;
import org.hacklace.animator.displaybuffer.MixedDisplayBuffer;
import org.hacklace.animator.displaybuffer.ReferenceDisplayBuffer;
import org.hacklace.animator.displaybuffer.TextDisplayBuffer;
import org.hacklace.animator.enums.PredefinedAnimation;

public class HacklaceConfigManager {

	public final static String HACKLACE_CHARSET = "ISO-8859-1";

	private List<DisplayBuffer> list;

	public HacklaceConfigManager() {
		list = new ArrayList<DisplayBuffer>();
	}

	public void clear() {
		list.clear();
	}

	public void readStream(InputStream stream, ErrorContainer errorContainer)
			throws IOException {
		DataInputStream in = new DataInputStream(stream);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in, HACKLACE_CHARSET));
			String fullConfigLineString;
			int lineNumber = 0;
			loop: while ((fullConfigLineString = br.readLine()) != null) {
				lineNumber++;
				if (fullConfigLineString.trim().equals("")) {
					errorContainer
							.addError("Empty lines are not allowed. Line number: "
									+ lineNumber);
					continue loop;
				}
				FullConfigLine fullLine = new FullConfigLine(
						fullConfigLineString);
				ErrorContainer lineErrorContainer = new ErrorContainer();
				DisplayBuffer displayBuffer = DisplayBuffer
						.createBufferFromLine(fullLine, lineErrorContainer);
				if (lineErrorContainer.containsErrorsOrWarnings()) {
					errorContainer.addWarning("There is a problem in line "
							+ lineNumber);
					errorContainer.addAll(lineErrorContainer);
				}

				if (displayBuffer != null /* $00 = EOF */) {
					list.add(displayBuffer);
				} else {
					break loop;
				}
			} // end loop
			if (fullConfigLineString == null) {
				errorContainer.addError("File must end with $00,");
			}

		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	public void readFile(File file, ErrorContainer errorContainer)
			throws IOException {
		FileInputStream fstream = new FileInputStream(file);
		readStream(fstream, errorContainer);
	}

	public void writeFile(File file) throws IOException {
		OutputStreamWriter out = null;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			out = new OutputStreamWriter(fos, HACKLACE_CHARSET);
			out.write(getRawString());
		} finally {
			if (out != null)
				out.close();
		}
	}

	public String getRawString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (DisplayBuffer displayBuffer : this.list) {
			stringBuilder.append(displayBuffer.getFullConfigLine()).append("\n");
		}
		stringBuilder.append("$00,");
		return stringBuilder.toString();
	}

	private void addDisplayBuffer(DisplayBuffer buffer) {
		list.add(buffer);
	}

	public TextDisplayBuffer addTextDisplayBuffer() {
		TextDisplayBuffer tdb = new TextDisplayBuffer();
		addDisplayBuffer(tdb);
		return tdb;
	}

	public GraphicDisplayBuffer addGraphicDisplayBuffer() {
		GraphicDisplayBuffer gdb = new GraphicDisplayBuffer();
		addDisplayBuffer(gdb);
		return gdb;
	}

	/**
	 * 
	 * @param whichAnimation
	 *            The consumer has to make sure that this is a valid animation
	 *            reference, as the potential exception is suppressed
	 * @return
	 */
	public ReferenceDisplayBuffer addReferenceDisplayBuffer(
			PredefinedAnimation reference) {
		ReferenceDisplayBuffer rdb = null;
		rdb = new ReferenceDisplayBuffer(reference);
		assert (rdb != null);
		addDisplayBuffer(rdb);
		return rdb;
	}

	public MixedDisplayBuffer addMixedDisplayBuffer() {
		MixedDisplayBuffer mdb = new MixedDisplayBuffer();
		addDisplayBuffer(mdb);
		return mdb;

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

	/**
	 * copies the display buffer, inserts it at the end of the list and returns
	 * it
	 * 
	 * @param index
	 * @return
	 */
	public DisplayBuffer copyDisplayBuffer(int index) {
		DisplayBuffer copy = list.get(index).clone();
		list.add(copy);
		return copy;
	}

	public List<DisplayBuffer> getList() {
		return list;
	}

	public DisplayBuffer getDisplayBuffer(int index) {
		return list.get(index);
	}

	public int getNumBytes() {
//		FlashExporter flashExporter = new FlashExporter();
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		try {
//			// Note: There is not much we can do about the exceptions here. They
//			// should never occur!
//			flashExporter.writeTo(getRawString(), out);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return out.size();
		int sum = 0;
		for (DisplayBuffer x : this.list) {
			sum += x.getNumBytes();
		}
		sum += 1; // final $00
		return sum;
	}
}
