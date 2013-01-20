/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;

import org.hacklace.animator.gui.actions.LedObserver;

public class Led extends JButton implements LedInterface {

	private static final long serialVersionUID = -5214373326373158893L;

	public final int row;
	public final int column;
	private boolean on;
	private List<LedObserver> observerList;

	public Led(int row, int column, LedObserver o) {
		setOpaque(true);
		setForeground(new Color(64, 255, 64));
		this.row = row;
		this.column = column;
		observerList = new LinkedList<LedObserver>();
		addObserver(o);
	}

	public void addObserver(LedObserver o) {
		observerList.add(o);
	}

	private void set() {
		on = true;
	}

	private void unset() {
		on = false;
	}

	@Override
	public void toggleByClick() {
		if (on) {
			unset();
		} else {
			set();
		}
		// there is currently only one observer, the EditPanel
		for (LedObserver o : observerList) {
			o.onLedChange(column, row, on);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		if (d.width > d.height) {
			d.width = d.height;
		} else if (d.height > d.width) {
			d.height = d.width;
		}
		return d;
	}

	@Override
	public void setFromBuffer(boolean on) {
		if (on) {
			set();
		} else {
			unset();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = ((Graphics2D) g);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Ellipse2D.Double circle = new Ellipse2D.Double(1, 1, getWidth() - 3,
				getHeight() - 3);
		if (on) {
			g2d.setColor(getForeground());
			g2d.fill(circle);
		} else {
			g2d.setColor(getBackground());
			g2d.fill(circle);
		}
	}

}
