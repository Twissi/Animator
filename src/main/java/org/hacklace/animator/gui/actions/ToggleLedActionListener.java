/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui.actions;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.hacklace.animator.gui.LedInterface;

public class ToggleLedActionListener extends MouseAdapter {

	private LedInterface led;

	public ToggleLedActionListener(LedInterface led) {
		this.led = led;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		led.toggleByClick();
	}

}
