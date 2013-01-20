/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import org.hacklace.animator.FontUtil;

public class VirtualKeyboardButton extends JButton {
	private static final long serialVersionUID = -740174075289099487L;

	private int imageIndex;
	private BufferedImage image;

	public VirtualKeyboardButton(int index) {
		super();
		setPreferredSize(new Dimension(24, 24));
		setFocusable(false);
		imageIndex = index;
		image = new BufferedImage(5, 7, BufferedImage.TYPE_INT_ARGB);
		// paint the font data to the image
		int[] animationBytes = FontUtil.getFiveBytesForIndex(index);
		for (int i = 0; i < animationBytes.length; i++) {
			for (int bit = 0; bit < 7; bit++) {
				if (i < animationBytes.length) {
					if ((animationBytes[i] & (int) Math.pow(2, bit)) != 0) {
						image.setRGB(i, bit, Color.black.getRGB());
					}
				}
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 4, 4, getWidth() - 4, getHeight() - 4, 0, 0, 5, 7,
				null);
	}

	public String getString() {
		return FontUtil.getRawStringForIndex(imageIndex);
	}

}
