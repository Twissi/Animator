package org.hacklace.animator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;

import junit.framework.TestCase;
import junitx.framework.FileAssert;

public class HacklaceConfigManagerTest extends TestCase {
	
	private File exampleConf;
	private File output;
	private HacklaceConfigManager manager;
	
	protected void setUp() throws IOException {
		manager = new HacklaceConfigManager();
		
		output =  Files.createTempFile("test", null ).toFile();
		URL url = this.getClass().getResource("/configs/example.hack");
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
	
	/**
	 * Test Hex-Sequence validation method
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public void testHexValidation() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> hcm = HacklaceConfigManager.class;
		Method hexSequence = hcm.getDeclaredMethod("isHexSequence",String.class);
		hexSequence.setAccessible(true);
		assertTrue((Boolean) hexSequence.invoke(null,"$FF"));
		assertTrue((Boolean) hexSequence.invoke(null,"$AC"));
		assertTrue((Boolean) hexSequence.invoke(null,"$45"));
		assertTrue((Boolean) hexSequence.invoke(null,"$47"));
		assertTrue((Boolean) hexSequence.invoke(null,"$3B"));
		assertFalse((Boolean) hexSequence.invoke(null," 3B"));
		assertFalse((Boolean) hexSequence.invoke(null,"3B"));
		assertFalse((Boolean) hexSequence.invoke(null,"$3b"));
		assertFalse((Boolean) hexSequence.invoke(null,"$ff"));
	}

}
