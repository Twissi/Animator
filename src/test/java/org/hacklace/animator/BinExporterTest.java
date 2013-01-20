/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;
import junitx.framework.FileAssert;

import org.hacklace.animator.exporter.BinExporter;
import org.junit.Test;

public class BinExporterTest extends TestCase {

	private File exampleConf;
	private File exampleFlash;
	private File output;
	private HacklaceConfigManager manager;
	private BinExporter binExporter;
	
	@Override
	protected void setUp() throws IOException {
		manager = new HacklaceConfigManager();
		output = File.createTempFile("test.bin", null);
		URL url = this.getClass().getResource("/configs/example.hack");
		exampleConf = new File(url.getFile());
		url = this.getClass().getResource("/configs/example.bin");
		exampleFlash = new File(url.getFile());
		binExporter = new BinExporter();
	}
	
	@Override
	protected void tearDown() {
		output.delete();
		exampleConf = null;
		exampleFlash = null;
	}
	
	@Test
	public void testReadEqualsWrite()
			throws IOException {
		manager.readFile(exampleConf, new ErrorContainer());
		InputStream stream = new ByteArrayInputStream(manager.getRawString().getBytes(
				HacklaceConfigManager.HACKLACE_CHARSET));
		binExporter.write(stream, output);
		if (output.length() < 256) {
			// pad with zeros
			long toWrite = 256 - output.length();
			FileOutputStream fos = new FileOutputStream(output, true);
			while (toWrite-- > 0) fos.write(0);
			fos.close();
		}
		FileAssert.assertBinaryEquals(exampleFlash, output);
	}

}
