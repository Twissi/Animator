package org.hacklace.animator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import junit.framework.TestCase;
import junitx.framework.FileAssert;

public class ConfigManagerTest extends TestCase {
	
	private File exampleConf;
	private File output;
	private ConfigManager manager;
	
	protected void setUp() throws IOException {
		manager = new ConfigManager();
		
		output =  Files.createTempFile("test", null ).toFile();
		URL url = this.getClass().getResource("/configs/example.cfg");
		exampleConf = new File(url.getFile());		
	}
	
	protected void tearDown() {
		output.delete();
		exampleConf = null;
	}
	
	/**
	 * Reads a config file into a ConfigManager object and writes it then
	 * to disk. These files have to be binary equal.
	 */
//	public void testReadEqualsWrite() {
//		manager.readFile(exampleConf);
//		manager.writeFile(output);
//		FileAssert.assertBinaryEquals(exampleConf, output);		
//	}
	
	public void testFoo() {
		
	}

}
