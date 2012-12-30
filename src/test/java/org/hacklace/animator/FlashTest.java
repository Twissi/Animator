package org.hacklace.animator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;
import junitx.framework.FileAssert;

import org.hacklace.animator.exporter.FlashExporter;
import org.junit.Test;

public class FlashTest extends TestCase {

	private File exampleConf;
	private File exampleFlash;
	private File output;
	private HacklaceConfigManager manager;
	private FlashExporter flashExporter;
	
	protected void setUp() throws IOException {
		manager = new HacklaceConfigManager();
		output = File.createTempFile("test.hack", null);
		URL url = this.getClass().getResource("/configs/example.hack");
		exampleConf = new File(url.getFile());
		url = this.getClass().getResource("/configs/example.flash");
		exampleFlash = new File(url.getFile());
		flashExporter = new FlashExporter();
	}
	
	protected void tearDown() {
		output.delete();
		exampleConf = null;
		exampleFlash = null;
	}
	
	@Test
	public void testReadEqualsWrite()
			throws IllegalHacklaceConfigFileException, IOException {
		manager.readFile(exampleConf);
		InputStream stream = new ByteArrayInputStream(manager.getRawString().getBytes(
				HacklaceConfigManager.HACKLACE_CHARSET));
		flashExporter.writeTo(stream, output);
		FileAssert.assertBinaryEquals(exampleFlash, output);
	}

}
