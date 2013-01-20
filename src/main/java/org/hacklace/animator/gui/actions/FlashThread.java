/*******************************************************************************
This program is made available under the terms of the GPLv3 or higher
which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
******************************************************************************/

package org.hacklace.animator.gui.actions;

import java.awt.Cursor;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.SwingWorker;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.exporter.FlashExporter;
import org.hacklace.animator.gui.AnimatorGui;

public class FlashThread extends SwingWorker<Void, Void> {

	private HacklaceConfigManager configManager;
	private AnimatorGui animatorGui;
	private FlashExporter flashExporter;

	private ProgressMonitorInputStream stream = null;

	public FlashThread(HacklaceConfigManager configManager,
			AnimatorGui animatorGui, FlashExporter flashExporter) {
		this.configManager = configManager;
		this.animatorGui = animatorGui;
		this.flashExporter = flashExporter;
	}

	@Override
	public Void doInBackground() {
		String rawString = configManager.getRawString();
		try {
			stream = new ProgressMonitorInputStream(null, (Object) "Flashing",
					new ByteArrayInputStream(rawString
							.getBytes(HacklaceConfigManager.HACKLACE_CHARSET)));
			ProgressMonitor progressMonitor = stream.getProgressMonitor();
			progressMonitor.setMaximum(rawString.length());
			progressMonitor.setProgress(0);
			progressMonitor.setMillisToPopup(0);
			animatorGui.setCursor(Cursor
					.getPredefinedCursor(Cursor.WAIT_CURSOR));
			flashExporter.write(stream);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error flashing hacklace: "
					+ ex, "Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	@Override
	public void done() {
		JOptionPane.showMessageDialog(null, // parent
				"Hacklace has been flashed.", // message
				"Flashed", // title
				JOptionPane.INFORMATION_MESSAGE); // type
		animatorGui.setCursor(null); // remove wait cursor
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException ex2) {
				ex2.printStackTrace();
			}
		}
	}

}
