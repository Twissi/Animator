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

import org.hacklace.animator.ConversionUtil;
import org.hacklace.animator.FontUtil;

public class VirtualKeyboardButton extends JButton {
	private static final long serialVersionUID = -740174075289099487L;

	private int imageIndex;
	private BufferedImage image;

	private static int COLS = AnimatorGui.getIniConf().columns(); // 5
	private static int ROWS = AnimatorGui.getIniConf().rows(); // 7

	public VirtualKeyboardButton(int indexInCharset) {
		super();
		setPreferredSize(new Dimension(24, 24));
		setFocusable(false);
		imageIndex = indexInCharset;
		image = new BufferedImage(COLS, ROWS, BufferedImage.TYPE_INT_ARGB);
		// paint the font data to the image
		int[] animationBytes = FontUtil.getFiveBytesForIndex(indexInCharset);
		for (int col = 0; col < animationBytes.length; col++) {
			for (int row = 0; row < ROWS; row++) {
				if (ConversionUtil.isBitSet(animationBytes[col], row)) {
					image.setRGB(col, row, Color.black.getRGB());

				}
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, // 
				4, 4, // x, y of top left corner of the destination rectangle
				getWidth() - 4, getHeight() - 4, // bottom right corner of
													// destination rectangle
				0, 0, // x, y of top left corner of source rectangle 
				5, 7, // x, y of bottom right corner of source rectangle
				null); // observer to be notified as more of the image is scaled
	}

	public String getRawString() {
		return FontUtil.getRawStringForIndex(imageIndex);
	}

}
