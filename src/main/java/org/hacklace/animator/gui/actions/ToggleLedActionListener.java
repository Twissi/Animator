/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.hacklace.animator.gui.LedInterface;

public class ToggleLedActionListener implements ActionListener {

	private LedInterface led;

	public ToggleLedActionListener(LedInterface button) {
		this.led = button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		led.toggleByClick();
	}

}
