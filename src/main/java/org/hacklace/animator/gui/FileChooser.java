/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class FileChooser extends JFileChooser {

	private JPanel contentPane;
	private File inputFile;
	private File outputFile;
	private String extension;

	private static final long serialVersionUID = 1L;

	/* variables */
	private File file;
	private FileFilter hacklaceFileFilter;
	/**
	 * constructor of FileChooser
	 */
	public FileChooser(String ext) {
		super();
		extension = ext;
	}

	/**
	 * This method returns a file which is meant to be the input-file.
	 * 
	 * @return returns the input-file
	 */
	public File inputFile() {
		hacklaceFileFilter = new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(extension);
			}
			@Override
			public String getDescription() {
				return extension;
			}
		};

		this.setFileFilter(hacklaceFileFilter);
		this.setMultiSelectionEnabled(false);
		int state = this.showOpenDialog(contentPane);
		if (state == JFileChooser.APPROVE_OPTION) {
			if (this.getSelectedFile().exists()) {
				file = this.getSelectedFile();
				return file;
			} else {
				JOptionPane.showMessageDialog(contentPane, "File not existing!", "Error", JOptionPane.INFORMATION_MESSAGE);
				if (inputFile != null) {
					return inputFile;
				} else {
					return null;
				}
			}
		} else {
			if (inputFile != null) {
				return inputFile;
			} else {
				return null;
			}
		}
	}

	/**
	 * This method returns a file which is meant to be the output-file.
	 * 
	 * @return returns the output-file
	 */
	public File outputFile() {
		int state = this.showSaveDialog(contentPane);
		if (state == JFileChooser.APPROVE_OPTION) {
			file = this.getSelectedFile();
			if (!(this.getSelectedFile().getName().toLowerCase().endsWith(extension))) {
				file = new File(file.getPath() + extension);
			}
			return file;
		} else {
			if (outputFile != null) {
				return outputFile;
			} else {
				return null;
			}
		}
	}

}
