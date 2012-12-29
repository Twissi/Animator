package org.hacklace.animator.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import org.hacklace.animator.displaybuffer.FontUtil;

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

	/* TESTING:
	public static void main(String[] args) {
		JFrame aFrame = new JFrame();
		aFrame.setLayout(new GridLayout());
		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aFrame.setPreferredSize(new Dimension(600, 100));
		for (int i = 0; i < 10; i++) {
			VirtualKeyboardButton button = new VirtualKeyboardButton(0x80 + i);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					VirtualKeyboardButton button = (VirtualKeyboardButton) arg0
							.getSource();
					System.out.println("Pressed: " + button.getString());
				}
			});
			aFrame.add(button);
		}
		aFrame.pack();
		aFrame.setVisible(true);
	}
	 */
}
