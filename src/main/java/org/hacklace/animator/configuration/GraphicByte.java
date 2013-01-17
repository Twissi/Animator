package org.hacklace.animator.configuration;

import java.util.List;

import org.hacklace.animator.ConversionUtil;
import org.hacklace.animator.ErrorContainer;

public class GraphicByte extends ByteElement {
	
	public GraphicByte(String fourChars, ErrorContainer errorContainer) {
		super(fourChars, errorContainer);
	}
	
	@Override
	public boolean isValid(ErrorContainer errorContainer) {
		if (super.isValid(errorContainer)) {
			return false;
		}
		if (getByteAsInt(new ErrorContainer()) == 0xFF) { // use different error container and ignore
			errorContainer.addError("$FF not allowed as graphic byte, as it switches direct mode.");
		}
		return true;
	}
	
	@Override
	public List<String> analyzeErrors() {
		List<String> list = super.analyzeErrors();
	    if (getByte(new ErrorContainer()) == 0xFF) // TODO make error handling more consistent 
	    	list.add("Graphic byte must not be FF as this switches direct mode.");
		return list;
	}
	
	@Override
	public List<String> analyzeWarnings() {
		List<String> list = super.analyzeWarnings();
		// highest bit should not be set
		if (getByte(new ErrorContainer()) < 0) // TODO make error handling more consistent 
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
		return ConversionUtil.isBitSet(getByte(new ErrorContainer()), index); // TODO maybe handle error?
	}

	public int getNumColumns() {
		return 1;
	}
	
	public int getNumBytes() {
		return 1;
	}

}
