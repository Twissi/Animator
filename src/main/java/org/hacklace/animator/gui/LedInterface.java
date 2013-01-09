package org.hacklace.animator.gui;


public interface LedInterface {
	/**
	 * must not notify the observer (which would set the buffer again)
	 * @param value
	 */
	public void setFromBuffer(boolean value);

	/**
	 * must notify the observer (which sets the buffer)
	 */
	public void toggleByClick();
	
	public boolean isOn();
	
	public int getRow();
	
	public int getColumn();

}
