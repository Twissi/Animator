package org.hacklace.animator;

public class ConversionUtil {

	/**
	 * 
	 * @param threeCharString
	 *            three characters $nn where n is hex (0-F)
	 * @return the byte represented by this (-128 to 127)
	 */
	public static byte convertStringToByte(String threeCharString) {
		assert (threeCharString.length() == 3);
		assert (threeCharString.charAt(0) == '$');
		return (byte) Integer.parseInt(threeCharString.substring(1), 16);
	}

	/**
	 * 
	 * @param threeCharString
	 *            three characters $nn where n is hex (0-F)
	 * @return the byte represented by this (0 to 255)
	 */
	public static int convertStringToInt(String threeCharString) {
		assert (threeCharString.length() == 3);
		assert (threeCharString.charAt(0) == '$');
		return Integer.parseInt(threeCharString.substring(1), 16);
	}

	/**
	 * 
	 * @param potentialHexString
	 *            a String of length 3
	 * @return true if string is $nn with n hex (0-F), false if the length of
	 *         the String is not 3 or if it does not start with $ or if the last
	 *         two chars are not hex
	 */
	public static boolean isHexSequence(String potentialHexString) {
		if (potentialHexString.length() != 3)
			return false;
		return potentialHexString.matches("^\\$[0-9A-F]{2}$");
		// ...................................$nn (exactly 3 chars)
	}

	/**
	 * 
	 * @param number
	 *            -128 to 127
	 * @return $nn a String of length 3
	 */
	public static String convertByteToString(byte number) {
		int value = number;
		if (value < 0)
			value += 256;
		String leadingZero = (value < 0x10) ? "0" : "";
		return "$" + leadingZero + Integer.toString(value, 16).toUpperCase();
	}

	/**
	 * 
	 * @param numbers
	 * @return e.g. "$74 $28 $32 " (always a trailing space)
	 */
	public static String convertBytesToString(byte[] numbers) {
		StringBuilder sb = new StringBuilder();
		for (byte number : numbers) {
			sb.append(convertByteToString(number)).append(" ");;
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param aniString
	 * @param lineNumber
	 * @return a byte array with one byte for each hex sequence
	 * @throws IllegalHacklaceConfigFileException
	 */
	public static byte[] createByteArrayFromString(String aniString,
			int lineNumber) throws IllegalHacklaceConfigFileException {
		final String separators = "[ ,;./:_+*|]";
		byte[] aniBytes = new byte[200];
		String[] aniByteStrings = aniString.split(separators);
		if (aniBytes.length > 200) {
			throw new IllegalHacklaceConfigFileException(
					"Illegal hacklace configuration file: More than 200 bytes in line "
							+ lineNumber + ".");

		}
		int index = 0;

		for (String aniByteString : aniByteStrings) {

			byte aniByte = ConversionUtil.convertStringToByte(aniByteString);
			aniBytes[index] = aniByte;
			index++;

		}
		return aniBytes;

	}

	/**
	 * The first bit (bit 7) is assumed to be 0 (should be the case for
	 * animation bytes)
	 * 
	 * @param aniByte
	 * @return array with 7 booleans
	 */
	public static boolean[] convertAnimationByteTo7Booleans(byte aniByte) {
		byte mask = 64; // 0100 0000
		boolean[] returnArray = new boolean[7];
		for (int row = 6; row >= 0; row--) {
			byte maskResult = (byte) (mask & aniByte);
			returnArray[row] = ((maskResult != 0) ? true : false);
			mask >>= 1;
		}
		return returnArray;
	}

}
