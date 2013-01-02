package org.hacklace.animator.displaybuffer;

import java.util.List;

import org.hacklace.animator.ConversionUtil;

public class GraphicByte extends ByteElement implements Size {
	
	public GraphicByte(String fourChars) {
		super(fourChars);
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && getByteAsInt() != 0xFF; 
	}
	
	@Override
	public List<String> analyzeErrors() {
		List<String> list = super.analyzeErrors();
	    if (getByte() == 0xFF) 
	    	list.add("Graphic byte must not be FF as this switches direct mode.");
		return list;
	}
	
	@Override
	public List<String> analyzeWarnings() {
		List<String> list = super.analyzeWarnings();
		if (getByte() < 0) // highest bit should not be set
			list.add("Highest bit should not be set. ("+fourChars+")");
		return list;
	}
	
	/**
	 * 
	 * @param index 0..6
	 * @return
	 */
	public boolean getBit(int index) {
		assert (index >= 0 && index <= 6);
		return ConversionUtil.isBitSet(getByte(), index);
	}

	@Override
	public int getNumColumns() {
		return 1;
	}
	
	@Override
	public int getNumBytes() {
		return 1;
	}

}
