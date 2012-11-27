package org.hacklace.animator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;

import junit.framework.TestCase;

import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.Delay;
import org.hacklace.animator.enums.Direction;
import org.hacklace.animator.enums.Speed;

public class HacklaceConfigManagerTest extends TestCase {

	private File exampleConf;
	private File output;
	private HacklaceConfigManager manager;

	protected void setUp() throws IOException {
		manager = new HacklaceConfigManager();

		output = Files.createTempFile("test", null).toFile();
		URL url = this.getClass().getResource("/configs/example.hack");
		exampleConf = new File(url.getFile());
	}

	protected void tearDown() {
		output.delete();
		exampleConf = null;
	}

	/**
	 * Reads a config file into a ConfigManager object and writes it then to
	 * disk. These files have to be binary equal.
	 */
	// public void testReadEqualsWrite() {
	// manager.readFile(exampleConf);
	// manager.writeFile(output);
	// FileAssert.assertBinaryEquals(exampleConf, output);
	// }

	/**
	 * Test Hex-Sequence validation method
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void testHexValidation() throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class<?> hcm = HacklaceConfigManager.class;
		Method isHexSequenceMethod = hcm.getDeclaredMethod("isHexSequence",
				String.class);
		isHexSequenceMethod.setAccessible(true);
		assertTrue((Boolean) isHexSequenceMethod.invoke(null, "$FF"));
		assertTrue((Boolean) isHexSequenceMethod.invoke(null, "$AC"));
		assertTrue((Boolean) isHexSequenceMethod.invoke(null, "$45"));
		assertTrue((Boolean) isHexSequenceMethod.invoke(null, "$47"));
		assertTrue((Boolean) isHexSequenceMethod.invoke(null, "$3B"));
		assertFalse((Boolean) isHexSequenceMethod.invoke(null, " 3B"));
		assertFalse((Boolean) isHexSequenceMethod.invoke(null, "3B"));
		assertFalse((Boolean) isHexSequenceMethod.invoke(null, "$3b"));
		assertFalse((Boolean) isHexSequenceMethod.invoke(null, "$ff"));
	}

	public void testConvertStringToByte() throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class<?> hcm = HacklaceConfigManager.class;
		Method convertStringToByteMethod = hcm.getDeclaredMethod(
				"convertStringToByte", String.class);
		convertStringToByteMethod.setAccessible(true);
		assertEquals((byte) 0, convertStringToByteMethod.invoke(null, "$00"));
		assertEquals((byte) 255, convertStringToByteMethod.invoke(null, "$FF"));
	}

	public void testCreateStatusByte() throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class<?> hcm = HacklaceConfigManager.class;
		Method createStatusByteFromStringMethod = hcm.getDeclaredMethod(
				"createStatusByteFromString", String.class, Integer.TYPE);
		createStatusByteFromStringMethod.setAccessible(true);
		StatusByte statusByte = (StatusByte) createStatusByteFromStringMethod
				.invoke(null, "$45", 0);
		assertEquals(Direction.FORWARD, statusByte.getDirection());
		assertEquals(Speed.FIVE, statusByte.getSpeed());
		assertEquals(Delay.FOUR, statusByte.getDelay());
		assertEquals(AnimationType.TEXT, statusByte.getAnimationType());

		statusByte = (StatusByte) createStatusByteFromStringMethod.invoke(null,
				"$00", 0);
		assertTrue(statusByte.isEOF());
	}

	public void testCreateByteArrayFromString() throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		
		Class<?> hcm = HacklaceConfigManager.class;
		Method createByteArrayFromStringMethod = hcm.getDeclaredMethod(
				"createByteArrayFromString", String.class, Integer.TYPE);
		createByteArrayFromStringMethod.setAccessible(true);
		byte[] aniBytes = (byte[]) createByteArrayFromStringMethod
				.invoke(null, "$FF $55 $2A $55 $2A $55 $2A $55 $2A $55 $2A $FF,", 0);
		assertEquals(200, aniBytes.length);
		assertEquals((byte)0xFF, aniBytes[0]);
		assertEquals((byte)0x55, aniBytes[1]);
		assertEquals((byte)0x2A, aniBytes[2]);
		assertEquals((byte)0x55, aniBytes[3]);
		assertEquals((byte)0x2A, aniBytes[4]);
		assertEquals((byte)0x55, aniBytes[5]);
		assertEquals((byte)0x2A, aniBytes[6]);
		assertEquals((byte)0x55, aniBytes[7]);
		assertEquals((byte)0x2A, aniBytes[8]);
		assertEquals((byte)0x55, aniBytes[9]);
		assertEquals((byte)0x2A, aniBytes[10]);
		assertEquals((byte)0xFF, aniBytes[11]);
		assertEquals((byte)0x00, aniBytes[12]);

	}

}