package org.hacklace.animator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.displaybuffer.GraphicDisplayBuffer;
import org.hacklace.animator.displaybuffer.TextDisplayBuffer;

public class ConfigManager {

	private List<DisplayBuffer> list;

	public ConfigManager() {
		list = new ArrayList<DisplayBuffer>();
	}

	public void readFile(File file) {
        
        // text or graphic animation?
        boolean isText = false;
        DisplayBuffer buffer;
        if (isText) {
        	TextDisplayBuffer textDisplayBuffer = new TextDisplayBuffer();
        	String text = ""; // TODO read text
        	textDisplayBuffer.setText(text);
        	buffer = textDisplayBuffer;
        }
        else
        {
        	GraphicDisplayBuffer graphicDisplayBuffer = new GraphicDisplayBuffer();
        	// TODO read animation
        	// TODO graphicDisplayBuffer.setFullGrid(...)
        	buffer = graphicDisplayBuffer; 
        }
        StatusByte statusByte = new StatusByte();
        // TODO read status byte
        buffer.setDirection(statusByte.getDirection());
        
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

}
