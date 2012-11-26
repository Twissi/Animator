package org.hacklace.animator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hacklace.animator.displaybuffer.DisplayBuffer;

public class ConfigManager {

	List<? extends DisplayBuffer> list;

	public ConfigManager() {
		list = new ArrayList<DisplayBuffer>();
	}

	public void readFile(File file) {

	}

	public void writeFile(File file) {

	}
	
	public void addDisplayBuffer(DisplayBuffer buffer) {
		
	}
	
	public void deleteDisplayBuffer(int index) {
		
	}
	
	public void moveUp(int index) {
		if (index == 0) {
			// do not throw an error, just do nothing
			return;
		}
		// actual code here
	}
	
	public void moveDown(int index) {
		if (index == list.size()-1) {
			// if index is already last, do nothing (no error)
			return;
		}
		// actual code here
	}
	
}
