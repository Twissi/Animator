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
import org.hacklace.animator.displaybuffer.MixedDisplayBuffer;
import org.hacklace.animator.displaybuffer.ReferenceDisplayBuffer;
import org.hacklace.animator.displaybuffer.TextDisplayBuffer;

public class HacklaceConfigManager {

	private List<DisplayBuffer> list;

	public HacklaceConfigManager() {
		list = new ArrayList<DisplayBuffer>();
	}
	
	public void clear() {
		list.clear();
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
				DisplayBuffer displayBuffer = DisplayBuffer.createBufferFromLine(cfgLine,
						lineNumber);
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

	public void writeFile(File file) throws IOException {
		BufferedWriter out = null;
		try {
			FileWriter fw = new FileWriter(file);
			out = new BufferedWriter(fw);

			for (DisplayBuffer displayBuffer : this.list) {
				out.write(displayBuffer.getRawString());
			}
			out.write("$00,");
		} finally {
			if (out != null)
				out.close();
		}
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


}
