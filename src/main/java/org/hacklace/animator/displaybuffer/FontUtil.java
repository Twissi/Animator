package org.hacklace.animator.displaybuffer;

import static org.hacklace.animator.ConversionUtil.convertStringToInt;
import static org.hacklace.animator.ConversionUtil.isHexSequence;

import java.util.LinkedList;
import java.util.List;

import org.hacklace.animator.ErrorContainer;

public class FontUtil {

	public final static int LOWEST_INDEX = 0x20;
	public final static int HIGHEST_INDEX = 0xAF;
	public final static int LOWEST_SPECIAL_INDEX = 0x80;
	public final static int SPECIAL_CHAR_OFFSET = 63;

	// warum int für die Bytes? Weil byte nicht von 0 bis 255, sondern von -128
	// bis 127 geht, also 0x80 ein Problem macht
	private final static int[][] HACKLACE_CHARSET = {
			// !"#$%&'()*+,-./0123456789:;<=>?
			// @ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_
			// `abcdefghijklmnopqrstuvwxyz{|}~
			{ 0x00, 0x00, 0x00, 0x80, 0x80 }, // 20 = 32 = space(3)
			{ 0x5F, 0x80, 0x80, 0x80, 0x80 }, // 21 = 33 = !
			{ 0x03, 0x00, 0x03, 0x80, 0x80 }, // 22 = 34 = "
			{ 0x14, 0x7F, 0x14, 0x7F, 0x14 }, // 23 = 35 = #
			{ 0x24, 0x2A, 0x7F, 0x2A, 0x12 }, // 24 = 36 = $
			{ 0x23, 0x13, 0x08, 0x64, 0x62 }, // 25 = 37 = %
			{ 0x36, 0x49, 0x56, 0x20, 0x50 }, // 26 = 38 = &
			{ 0x05, 0x03, 0x80, 0x80, 0x80 }, // 27 = 39 = '
			{ 0x1C, 0x22, 0x41, 0x80, 0x80 }, // 28 = 40 = (
			{ 0x41, 0x22, 0x1C, 0x80, 0x80 }, // 29 = 41 = )
			{ 0x22, 0x14, 0x6B, 0x14, 0x22 }, // 2A = 42 = *
			{ 0x08, 0x08, 0x3E, 0x08, 0x08 }, // 2B = 43 = +
			{ 0x50, 0x30, 0x80, 0x80, 0x80 }, // 2C = 44 = ,
			{ 0x08, 0x08, 0x08, 0x08, 0x80 }, // 2D = 45 = -
			{ 0x60, 0x60, 0x80, 0x80, 0x80 }, // 2E = 46 = .
			{ 0x60, 0x10, 0x08, 0x04, 0x03 }, // 2F = 47 = /
			{ 0x3E, 0x41, 0x41, 0x3E, 0x80 }, // 30 = 48 = 0
			{ 0x42, 0x7F, 0x40, 0x80, 0x80 }, // 31 = 49 = 1
			{ 0x62, 0x51, 0x49, 0x46, 0x80 }, // 32 = 50 = 2
			{ 0x22, 0x41, 0x49, 0x36, 0x80 }, // 33 = 51 = 3
			{ 0x18, 0x14, 0x12, 0x7F, 0x80 }, // 34 = 52 = 4
			{ 0x27, 0x45, 0x45, 0x39, 0x80 }, // 35 = 53 = 5
			{ 0x3C, 0x4A, 0x49, 0x31, 0x80 }, // 36 = 54 = 6
			{ 0x01, 0x71, 0x0D, 0x03, 0x80 }, // 37 = 55 = 7
			{ 0x36, 0x49, 0x49, 0x36, 0x80 }, // 38 = 56 = 8
			{ 0x06, 0x49, 0x29, 0x1E, 0x80 }, // 39 = 57 = 9
			{ 0x36, 0x36, 0x80, 0x80, 0x80 }, // 3A = 58 = :
			{ 0x56, 0x36, 0x80, 0x80, 0x80 }, // 3B = 59 = ;
			{ 0x08, 0x14, 0x22, 0x41, 0x80 }, // 3C = 60 = <
			{ 0x14, 0x14, 0x14, 0x14, 0x80 }, // 3D = 61 = =
			{ 0x41, 0x22, 0x14, 0x08, 0x80 }, // 3E = 62 = >
			{ 0x02, 0x51, 0x09, 0x06, 0x80 }, // 3F = 63 = ?
			{ 0x32, 0x49, 0x79, 0x41, 0x3E }, // 40 = 64 = @
			{ 0x7E, 0x09, 0x09, 0x7E, 0x80 }, // 41 = 65 = A
			{ 0x7F, 0x49, 0x49, 0x36, 0x80 }, // 42 = 66 = B
			{ 0x3E, 0x41, 0x41, 0x22, 0x80 }, // 43 = 67 = C
			{ 0x7F, 0x41, 0x41, 0x3E, 0x80 }, // 44 = 68 = D
			{ 0x7F, 0x49, 0x49, 0x41, 0x80 }, // 45 = 69 = E
			{ 0x7F, 0x09, 0x09, 0x01, 0x80 }, // 46 = 70 = F
			{ 0x3E, 0x49, 0x49, 0x3A, 0x80 }, // 47 = 71 = G
			{ 0x7F, 0x08, 0x08, 0x7F, 0x80 }, // 48 = 72 = H
			{ 0x41, 0x7F, 0x41, 0x80, 0x80 }, // 49 = 73 = I
			{ 0x20, 0x40, 0x40, 0x3F, 0x80 }, // 4A = 74 = J
			{ 0x7F, 0x08, 0x14, 0x63, 0x80 }, // 4B = 75 = K
			{ 0x7F, 0x40, 0x40, 0x40, 0x80 }, // 4C = 76 = L
			{ 0x7F, 0x02, 0x0C, 0x02, 0x7F }, // 4D = 77 = M
			{ 0x7F, 0x06, 0x18, 0x7F, 0x80 }, // 4E = 78 = N
			{ 0x3E, 0x41, 0x41, 0x3E, 0x80 }, // 4F = 79 = O
			{ 0x7F, 0x09, 0x09, 0x06, 0x80 }, // 50 = 80 = P
			{ 0x3E, 0x41, 0x21, 0x5E, 0x80 }, // 51 = 81 = Q
			{ 0x7F, 0x09, 0x19, 0x66, 0x80 }, // 52 = 82 = R
			{ 0x26, 0x49, 0x49, 0x32, 0x80 }, // 53 = 83 = S
			{ 0x01, 0x01, 0x7F, 0x01, 0x01 }, // 54 = 84 = T
			{ 0x3F, 0x40, 0x40, 0x3F, 0x80 }, // 55 = 85 = U
			{ 0x07, 0x18, 0x60, 0x18, 0x07 }, // 56 = 86 = V
			{ 0x3F, 0x40, 0x38, 0x40, 0x3F }, // 57 = 87 = W
			{ 0x63, 0x14, 0x08, 0x14, 0x63 }, // 58 = 88 = X
			{ 0x03, 0x04, 0x78, 0x04, 0x03 }, // 59 = 89 = Y
			{ 0x61, 0x59, 0x45, 0x43, 0x80 }, // 5A = 90 = Z
			{ 0x7F, 0x41, 0x41, 0x80, 0x80 }, // 5B = 91 = [
			{ 0x03, 0x04, 0x08, 0x10, 0x60 }, // 5C = 92 = \
			{ 0x41, 0x41, 0x7F, 0x80, 0x80 }, // 5D = 93 = ]
			{ 0x02, 0x01, 0x02, 0x80, 0x80 }, // 5E = 94 = ^
			{ 0x40, 0x40, 0x40, 0x40, 0x80 }, // 5F = 95 = _
			{ 0x03, 0x04, 0x80, 0x80, 0x80 }, // 60 = 96 = `
			{ 0x20, 0x54, 0x54, 0x78, 0x80 }, // 61 = 97 = a
			{ 0x7F, 0x48, 0x48, 0x30, 0x80 }, // 62 = 98 = b
			{ 0x38, 0x44, 0x44, 0x28, 0x80 }, // 63 = 99 = c
			{ 0x38, 0x44, 0x44, 0x7F, 0x80 }, // 64 = 100 = d
			{ 0x38, 0x54, 0x54, 0x48, 0x80 }, // 65 = 101 = e
			{ 0x04, 0x7E, 0x05, 0x01, 0x80 }, // 66 = 102 = f
			{ 0x48, 0x54, 0x54, 0x38, 0x80 }, // 67 = 103 = g
			{ 0x7F, 0x08, 0x08, 0x70, 0x80 }, // 68 = 104 = h
			{ 0x7A, 0x80, 0x80, 0x80, 0x80 }, // 69 = 105 = i
			{ 0x20, 0x40, 0x3A, 0x80, 0x80 }, // 6A = 106 = j
			{ 0x7F, 0x08, 0x14, 0x62, 0x80 }, // 6B = 107 = k
			{ 0x41, 0x7F, 0x40, 0x80, 0x80 }, // 6C = 108 = l
			{ 0x7C, 0x04, 0x78, 0x04, 0x78 }, // 6D = 109 = m
			{ 0x7C, 0x04, 0x04, 0x78, 0x80 }, // 6E = 110 = n
			{ 0x38, 0x44, 0x44, 0x38, 0x80 }, // 6F = 111 = o
			{ 0x7C, 0x24, 0x24, 0x18, 0x80 }, // 70 = 112 = p
			{ 0x18, 0x24, 0x24, 0x7C, 0x80 }, // 71 = 113 = q
			{ 0x78, 0x04, 0x04, 0x80, 0x80 }, // 72 = 114 = r
			{ 0x48, 0x54, 0x54, 0x24, 0x80 }, // 73 = 115 = s
			{ 0x04, 0x3F, 0x44, 0x44, 0x80 }, // 74 = 116 = t
			{ 0x3C, 0x40, 0x40, 0x7C, 0x80 }, // 75 = 117 = u
			{ 0x0C, 0x30, 0x40, 0x30, 0x8C }, // 76 = 118 = v
			{ 0x3C, 0x40, 0x30, 0x40, 0x3C }, // 77 = 119 = w
			{ 0x44, 0x28, 0x10, 0x28, 0x44 }, // 78 = 120 = x
			{ 0x4C, 0x50, 0x50, 0x3C, 0x80 }, // 79 = 121 = y
			{ 0x64, 0x54, 0x4C, 0x44, 0x80 }, // 7A = 122 = z
			{ 0x08, 0x36, 0x41, 0x80, 0x80 }, // 7B = 123 = {
			{ 0x7F, 0x80, 0x80, 0x80, 0x80 }, // 7C = 124 = |
			{ 0x41, 0x36, 0x08, 0x80, 0x80 }, // 7D = 125 = }
			{ 0x08, 0x04, 0x08, 0x10, 0x08 }, // 7E = 126 = ~
			{ 0x80, 0x80, 0x80, 0x80, 0x80 }, // 7F = 127 = nothing
			{ 0x1C, 0x2A, 0x49, 0x49, 0x22 }, // 80 = 128 = ^A = €
			{ 0x1F, 0x04, 0x7F, 0x40, 0x40 }, // 81 = 129 = ^B = HL sign
			{ 0x20, 0x12, 0x10, 0x12, 0x20 }, // 82 = 130 = ^C = unhappy smiley
			{ 0x10, 0x22, 0x20, 0x22, 0x10 }, // 83 = 131 = ^D = happy smiley
			{ 0x21, 0x54, 0x54, 0x79, 0x80 }, // 84 = 132 = ^E = ä
			{ 0x79, 0x14, 0x14, 0x79, 0x80 }, // 85 = 133 = ^F = Ä
			{ 0x39, 0x44, 0x44, 0x39, 0x80 }, // 86 = 134 = ^G = ö
			{ 0x39, 0x44, 0x44, 0x39, 0x80 }, // 87 = 135 = ^H = Ö
			{ 0x3D, 0x40, 0x40, 0x7D, 0x80 }, // 88 = 136 = ^I = ü
			{ 0x3D, 0x40, 0x40, 0x3D, 0x80 }, // 89 = 137 = ^J = Ü
			{ 0x7E, 0x25, 0x25, 0x1A, 0x80 }, // 8A = 138 = ^K = ß
			{ 0x6C, 0x1A, 0x6F, 0x1A, 0x6C }, // 8B = 139 = ^L = alien1
			{ 0x7D, 0x5A, 0x1E, 0x5A, 0x7D }, // 8C = 140 = ^M = alien2
			{ 0x4E, 0x7B, 0x0F, 0x7B, 0x4E }, // 8D = 141 = ^N = alien3
			{ 0x7C, 0x3A, 0x7E, 0x3A, 0x7C }, // 8E = 142 = ^O = alien4
			{ 0x1C, 0x76, 0x2E, 0x76, 0x1C }, // 8F = 143 = ^P = alien5
			{ 0x1E, 0x34, 0x7C, 0x34, 0x1E }, // 90 = 144 = ^Q = alien6
			{ 0x0C, 0x12, 0x24, 0x12, 0x0C }, // 91 = 145 = ^R = heart
			{ 0x08, 0x1C, 0x3E, 0x7F, 0x80 }, // 92 = 146 = ^S = triangle left
			{ 0x7F, 0x3E, 0x1C, 0x08, 0x80 }, // 93 = 147 = ^T = triangle right
			{ 0x30, 0x3F, 0x01, 0x62, 0x7E }, // 94 = 148 = ^U = two notes
			{ 0x30, 0x3F, 0x02, 0x80, 0x80 }, // 95 = 149 = ^V = one note
			{ 0x1E, 0x3D, 0x77, 0x73, 0x31 }, // 96 = 150 = ^W = Pacman
			{ 0x60, 0x7E, 0x7B, 0x7E, 0x60 }, // 97 = 151 = ^X = rocket (?)
			{ 0x20, 0x5F, 0x23, 0x80, 0x80 }, // 98 = 152 = ^Y = key
			{ 0x7E, 0x7A, 0x7A, 0x7F, 0x80 }, // 99 = 153 = ^Z = cell phone (?)
			{ 0x03, 0x45, 0x79, 0x45, 0x03 }, // 9A = 154 = ^[ = glass
			{ 0x10, 0x28, 0x24, 0x28, 0x10 }, // 9B = 155 = ^\ =
			{ 0x08, 0x14, 0x2A, 0x14, 0x08 }, // 9C = 156 = ^] = diamond
			{ 0x00, 0x00, 0x00, 0x00, 0x00 }, // 9D = 157 = (not ^^) = space(5)
			{ 0x36, 0x36, 0x08, 0x36, 0x36 }, // 9E = 158 = ^_ =
			{ 0x1E, 0x14, 0x3C, 0x28, 0x78 }, // 9F = 159 = ^` =
			{ 0x44, 0x24, 0x1D, 0x24, 0x44 }, // A0 = 160 = ^a = person1 arms
												// straight out
			{ 0x42, 0x24, 0x1D, 0x62, 0x01 }, // A1 = 161 = ^b = person2
			{ 0x08, 0x65, 0x1C, 0x22, 0x41 }, // A2 = 162 = ^c = person3
			{ 0x46, 0x24, 0x1D, 0x24, 0x4C }, // A3 = 163 = ^d = person4 right
												// hand up
			{ 0x08, 0x44, 0x3D, 0x44, 0x08 }, // A4 = 164 = ^e = person5 arms
												// down
			{ 0x4C, 0x24, 0x1D, 0x24, 0x46 }, // A5 = 165 = ^f = person6 left
												// hand
												// up
			{ 0x01, 0x62, 0x1D, 0x62, 0x01 }, // A6 = 166 = ^g = person 7 \o/
			{ 0x42, 0x24, 0x1D, 0x24, 0x42 }, // A7 = 167 = ^h = person8 \o/
			{ 0x7C, 0x46, 0x57, 0x46, 0x7C }, // A8 = 168 = ^i = house (?)
			{ 0x7F, 0x2A, 0x2A, 0x7F, 0x80 }, // A9 = 169 = ^j = ladder
			{ 0x2A, 0x7F, 0x41, 0x7F, 0x2A }, // AA = 170 = ^k =
			{ 0x0A, 0x00, 0x55, 0x00, 0x0A }, // AB = 171 = ^l =
			{ 0x30, 0x48, 0x4D, 0x33, 0x07 }, // AC = 172 = ^m = male
			{ 0x06, 0x29, 0x79, 0x29, 0x06 }, // AD = 173 = ^n = female
			{ 0x08, 0x1C, 0x2A, 0x08, 0x08 }, // AE = 174 = ^o = left arrow
			{ 0x08, 0x08, 0x2A, 0x1C, 0x08 } // / AF = 175 = ^p = right arrow
	};

	/**
	 * 
	 * @param index
	 *            a number between LOWEST_INDEX (0x20 i.e. decimal 32) and
	 *            HIGHEST_INDEX (0xAF i.e. decimal 175)
	 * @return the five animation bytes (including trailing empty columns)
	 */
	public static int[] getFiveBytesForIndex(int index) {
		int positiveIndex = index;
		if (positiveIndex < 0) positiveIndex += 256; 
		if (positiveIndex >= HIGHEST_INDEX) 
			positiveIndex = (int) '?'; // display a question mark
		return HACKLACE_CHARSET[positiveIndex - LOWEST_INDEX];
	}

	/**
	 * 
	 * @param c
	 *            an individual character like a, A, ä, ~, $, €, ., !
	 * @return the five animation bytes (including trailing empty columns) for
	 *         this character, the five bytes for ? (a question mark) for
	 *         unknown characters
	 */
	public static int[] getFiveBytesForChar(char c) {
		int n = (int) '?'; // if unkonwn, display question mark
		// ASCII
		if (c >= LOWEST_INDEX && c < LOWEST_SPECIAL_INDEX) {
			n = (int) c;
		}
		switch (c) {
		case '€':
			n = 0x80;
			break;
		case 'ä':
			n = 0x84;
			break;
		case 'Ä':
			n = 0x85;
			break;
		case 'ö':
			n = 0x86;
			break;
		case 'Ö':
			n = 0x87;
			break;
		case 'ü':
			n = 0x88;
			break;
		case 'Ü':
			n = 0x89;
			break;
		case 'ß':
			n = 0x8A;
			break;
		}
		return getFiveBytesForIndex(n);
	}

	/**
	 * 
	 * @param special
	 *            two-character String, first char is ^
	 * @return returns the five animation bytes for € for ^A etc.
	 */
	public static int[] getFiveBytesForSpecial(String special) {
		if (special.length() != 2)
			return getFiveBytesForIndex('?'); // if unkown, display question
												// mark
		assert (special.charAt(0) == '^');
		char c = special.charAt(1);
		if (c == '^')
			return getFiveBytesForChar('^');
		else
			return getFiveBytesForSpecial(c);
	}

	/**
	 * 
	 * @param c
	 *            the ASCII char after the ^
	 * @return returns the five animation bytes for € for ^A etc.
	 */
	public static int[] getFiveBytesForSpecial(char c) {
		int index = c + SPECIAL_CHAR_OFFSET;
		return getFiveBytesForIndex(index);
	}
	
	public static boolean isValidSpecialChar(char c) {
		int index = c + SPECIAL_CHAR_OFFSET;
		return index >= LOWEST_SPECIAL_INDEX && index <= HIGHEST_INDEX;
	}

	/**
	 * 
	 * @param fiveAnimationBytes
	 *            an int[5] array with animation bytes, possibly 1 to 4 "empty"
	 *            columns with value 0x80
	 * @return an array of one to five bytes, no trailing 0x80 (empty columns)
	 */
	private static int[] removeTrailingEmptyColumnsAndAddOneEmptyColumn(
			int[] fiveAnimationBytes) {
		int countNonEmptyColumns = 0;
		for (int i = 0; i < 5; i++) {
			if (fiveAnimationBytes[i] != 0x80)
				countNonEmptyColumns = i + 1;
		}
		int[] newAnimationBytes = new int[countNonEmptyColumns + 1];
		for (int i = 0; i < countNonEmptyColumns; i++) {
			newAnimationBytes[i] = fiveAnimationBytes[i];
		}
		// there will be one trailing empty column - nothing to do
		return newAnimationBytes;
	}

	/**
	 * 
	 * @param index
	 *            a number between LOWEST_INDEX (0x20 i.e. decimal 32) and
	 *            HIGHEST_INDEX (0xAF i.e. decimal 175)
	 * @return the two to six animation bytes (exactly one empty column in the
	 *         end) (one byte for $7F = empty)
	 */
	public static int[] getMinimumBytesForIndex(int index) {
		int positiveIndex = index;
		if (positiveIndex < 0) positiveIndex += 256; 
		return removeTrailingEmptyColumnsAndAddOneEmptyColumn(getFiveBytesForIndex(positiveIndex));
	}

	/**
	 * 
	 * @param c
	 *            an individual character like a, A, ä, ~, $, €, ., !
	 * @return the two to six animation bytes (exactly one trailing empty
	 *         column) for this character, the bytes for ? (a question mark) for
	 *         unknown characters
	 */
	public static int[] getMinimumBytesForChar(char c) {
		return removeTrailingEmptyColumnsAndAddOneEmptyColumn(getFiveBytesForChar(c));
	}

	/**
	 * 
	 * @param special
	 *            two-character String, first char is ^
	 * @return returns the two to six animation bytes for € for ^A etc.
	 */
	public static int[] getMinimumBytesForSpecial(String special) {
		return removeTrailingEmptyColumnsAndAddOneEmptyColumn(getFiveBytesForSpecial(special));
	}

	/**
	 * 
	 * @param c
	 *            the ASCII char after the ^
	 * @return returns the two to six animation bytes for € for ^A etc. (exactly
	 *         one trailing empty column
	 */
	public static int[] getMinimumBytesForSpecial(char c) {
		return removeTrailingEmptyColumnsAndAddOneEmptyColumn(getFiveBytesForSpecial(c));
	}

	/**
	 * 
	 * @param c
	 * @return true for ASCII (0x20 to 0x79), umlauts, ß and € (euro sign)
	 */
	public static boolean isValidHacklaceChar(char c) {
		// https://raumzeitlabor.de/w/images/d/da/Hacklace_Font_5x7_extended.bmp

		// ASCII (includes the special characters $ ^ and ~
		if (0x20 <= c && c <= 0x79) {
			return true;
		}

		// other letters that can be entered on a German keyboard
		if ("ÄäÖöÜüß€".indexOf(c) != -1) {
			return true;
		}

		// further special Hacklace characters are entered by ^B etc., so they
		// are already covered by ASCII above

		return false;
	}

	/**
	 * returns how the character with this index has to be represented in the
	 * config file
	 * 
	 * @param index
	 * @return e.g. $$, ^^, ~~, a, A,
	 */
	public static String getRawStringForIndex(int index) {
		// special treatment
		if (index == '$')
			return "$$";
		if (index == '^')
			return "^^";
		if (index == '~')
			return "~~";
		if (index == 0x7F)
			return "$7F,";
		if (index - SPECIAL_CHAR_OFFSET == '^')
			return "$9D,";

		// normal chars
		if (LOWEST_INDEX <= index && index < LOWEST_SPECIAL_INDEX)
			return "" + (char) index;

		// special chars
		if (LOWEST_SPECIAL_INDEX <= index && index <= HIGHEST_INDEX)
			return "^" + (char) (index - SPECIAL_CHAR_OFFSET);

		return "?";
	}

	/**
	 * 
	 * @param rawString
	 *            text from a configuration line e.g. Hellö x~~y 10 ^A. (Must be
	 *            textual - this is FontUtil after all)
	 * @return animation bytes - minimum bytes incl. one blank line for each
	 */
	public static int[] getIntsForRawString(String rawString, ErrorContainer errorContainer) {
		List<Integer> returnList = new LinkedList<Integer>();

		loopOverRawString: for (int i = 0; i < rawString.length(); i++) {
			int[] animationBytesForOneCharacter = null; // one to five bytes
			char c = rawString.charAt(i);

			// normal characters
			if (c != '~' && c != '^' && c != '$') {
				animationBytesForOneCharacter = getMinimumBytesForChar(c);
			}
			// escape characters ~, ^, $
			else {
				i++; // first
				// ignore escape character at the end of the string
				if (i > rawString.length() - 1)
					break loopOverRawString;
				char next = rawString.charAt(i);
				// escape for the character itself? I.e. ~~ ^^ $$
				if (next == c) { // Yes, ~~ ^^ $$
					animationBytesForOneCharacter = getMinimumBytesForChar(c);
				} else { // No, special chars or reference animation
					switch (c) {
					case '^': // (but not ^^) ^A for € etc.
						animationBytesForOneCharacter = FontUtil
								.getMinimumBytesForSpecial(next);
						break;
					case '$': // (but not $$) $80 for € etc.
						String charSetIndexAsThreeCharString = "$" + next;
						i++; // second
						// ignore if end of string
						if (i > rawString.length() - 1)
							break loopOverRawString;
						charSetIndexAsThreeCharString += rawString.charAt(i);
						if (isHexSequence(charSetIndexAsThreeCharString, errorContainer)) {
							int charSetIndex = convertStringToInt(charSetIndexAsThreeCharString, errorContainer);
							animationBytesForOneCharacter = getMinimumBytesForIndex(charSetIndex);
							i++; // third: there are actually four chars:
									// $nn, i.e. one separator
									// (separator is comma in default
									// config, but can be space and others)
						} else {
							// probably the user is in the process of typing
							i--; // undo second
							i--; // undo first
							// temporarily just display the $ until the user
							// has finished typing
							animationBytesForOneCharacter = getMinimumBytesForChar('$');
						}
						break;
					case '~':
						// TODO A reference to an animation in a text
						// animation... What do?
						// For a non-fatal fallback, display the escape
						// sequence (e.g. ~A) instead...
						i--;
						animationBytesForOneCharacter = getMinimumBytesForChar('~');
						break;
					}
				}
			}

			for (int aniByte : animationBytesForOneCharacter) {
				returnList.add(aniByte);
			} // end loop over animation bytes for one character

		} // end loop over text
		int[] returnArray = new int[returnList.size()];
		int i = 0;
		for (int animationByte : returnList) {
			returnArray[i++] = animationByte;
		}
		return returnArray;
	}

	/**
	 * 
	 * @param rawString
	 *            text from a configuration line e.g. Hellö x~~y 10 ^A
	 * @return animation bytes - minimum bytes incl. one blank column for each
	 */
	public static byte[] getBytesForRawString(String rawString, ErrorContainer errorContainer) {
		int[] intArray = getIntsForRawString(rawString, errorContainer);
		// convert int array to byte array
		byte[] returnArray = new byte[intArray.length];
		for (int i = 0; i < intArray.length; i++) {
			returnArray[i] = (byte) intArray[i];
		}
		return returnArray;
	}

}
