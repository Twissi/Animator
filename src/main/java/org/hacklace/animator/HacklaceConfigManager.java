package org.hacklace.animator;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;
import org.hacklace.animator.displaybuffer.MixedDisplayBuffer;
import org.hacklace.animator.displaybuffer.ReferenceDisplayBuffer;
import org.hacklace.animator.displaybuffer.TextDisplayBuffer;
import org.hacklace.animator.exporter.FlashExporter;

public class HacklaceConfigManager {

	public final static String HACKLACE_CHARSET = "ISO-8859-1";

	private List<DisplayBuffer> list;

	public HacklaceConfigManager() {
		list = new ArrayList<DisplayBuffer>();
	}

	public void clear() {
		list.clear();
	}

	public void readStream(InputStream stream)
			throws IllegalHacklaceConfigLineException, IOException {
		DataInputStream in = new DataInputStream(stream);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in, HACKLACE_CHARSET));
			String cfgLine;
			int lineNumber = 0;
			loop: while ((cfgLine = br.readLine()) != null) {
				lineNumber++;
				if (cfgLine.trim().equals("")) {
					// ignore empty lines (especially at end of file)
					continue loop;
				}
				DisplayBuffer displayBuffer = DisplayBuffer
						.createBufferFromLine(cfgLine, lineNumber);
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

	public void readFile(File file) throws IOException,
			IllegalHacklaceConfigLineException {
		FileInputStream fstream = new FileInputStream(file);
		readStream(fstream);
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
			stringBuilder.append(displayBuffer.getRawString()).append("\n");
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

	public ReferenceDisplayBuffer addReferenceDisplayBuffer(char whichAnimation) {
		ReferenceDisplayBuffer rdb = new ReferenceDisplayBuffer(whichAnimation);
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

	public int getBytesUsed() {
		FlashExporter flashExporter = new FlashExporter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in;
		try {
			// Note: There is not much we can do about the exceptions here. They
			// should never occur!
			in = new ByteArrayInputStream(getRawString().getBytes(
					HacklaceConfigManager.HACKLACE_CHARSET));
			flashExporter.writeTo(in, out);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.size();
	}

}
