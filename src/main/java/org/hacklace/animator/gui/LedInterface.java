/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
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
	
}
