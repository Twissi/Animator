package org.hacklace.animator.configuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hacklace.animator.ConversionUtil;
import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.displaybuffer.Size;
import org.hacklace.animator.enums.AnimationType;

/**
 * Hacklace configuration line without modus byte. Immutable
 * 
 * @author monika
 * 
 */

public class RestOfConfigLine implements Size {

	private String originalRawString;
	private String modifiedRawString;
	public final static String DIRECT = "$FF";

	/**
	 * 
	 * @param restOfLineString
	 *            may be invalid (but not null)
	 */
	public RestOfConfigLine(String restOfLineString, ErrorContainer errorContainer) {
		assert (restOfLineString != null);
		this.originalRawString = restOfLineString;
		getAnimationElements(errorContainer);
	}

	/**
	 * How often does $FF occur? (Once for each start and end of direct mode)
	 * 
	 * @param restOfLine
	 * @return
	 */
	int numberOfDirectModes() {
		int numberOfDirectModes = 0;
		int index = 0;
		while ((index = originalRawString.indexOf(DIRECT, index)) != -1) {
			numberOfDirectModes++;
			index += 3;
		}
		return numberOfDirectModes;
	}

	/**
	 * rest of line starts with $FF and ends with $FF, (or other char instead of
	 * ,)
	 * 
	 * @param fullLine
	 * @return
	 */
	boolean isGraphicOnly() {
		if (!originalRawString.startsWith(DIRECT))
			return false;
		int length = originalRawString.length();
		if (length < 4)
			return false;
		String end = originalRawString.substring(length - 4, length - 1);
		// last char is , or space, leave out
		if (!end.equals(RestOfConfigLine.DIRECT))
			return false;
		if (numberOfDirectModes() == 2)
			return true;
		return false;
	}

	/**
	 * 
	 * @param restOfLine
	 *            configuration line without the first four chars for the modus
	 *            byte
	 * @return
	 */
	public AnimationType analyzeType() {

		// 0 or 1 chars (plus spaces) cannot be reference/graphic/mixed
		if (originalRawString.trim().length() < 2)
			return AnimationType.TEXT;

		if (isReference())
			return AnimationType.REFERENCE;

		// contains, but is not reference
		if (containsReference())
			return AnimationType.MIXED;

		// contains no direct modes and no reference => is text
		if (!containsDirectMode())
			return AnimationType.TEXT;

		if (isGraphicOnly())
			return AnimationType.GRAPHIC;

		return AnimationType.MIXED;
	}

	boolean containsDirectMode() {
		return numberOfDirectModes() > 0;
	}

	/**
	 * 
	 * @param restOfLine
	 * @return true for ~A etc, false for ~~, false for others
	 */
	boolean isReference() {
		return originalRawString.startsWith("~")
				&& originalRawString.length() == 2
				&& originalRawString.charAt(1) != '~';
	}

	/**
	 * 
	 * @param line
	 *            can be rest of line (without modus byte) or full line (with
	 *            modus byte)
	 * @return
	 */
	boolean containsReference() {
		if (originalRawString == null)
			return false;
		if (originalRawString.length() < 2)
			return false;
		if (!originalRawString.contains("~"))
			return false;
		// line now certainly contains ~
		char last = ' ';
		for (char c : originalRawString.toCharArray()) {
			if (last == '~' && c != '~') {
				// ~~ is not a reference, everything else is
				return true;
			}
			if (last == '~' && c == '~') {
				last = ' ';
				// prevent e.g. ~~A to be read as reference instead of as text ~
				// and A
			} else {
				last = c;
			}
		}
		return false;
	}

	public String getOriginalRawString() {
		return originalRawString;
	}
	
	public String getModifiedRawString() {
		return modifiedRawString;
	}

	@Override
	public String toString() {
		return originalRawString;
	}

	/**
	 * the consumer has to ensure that this is only called on valid graphic-only
	 * configuration lines
	 * 
	 * @return essentially cuts off the first four and last four chars; "" for
	 *         restOfLines of length 8 and less
	 */

	public DirectMode getDirectMode() {
		return new DirectMode(this);
	}

	private List<AnimationPart> animationPartList = null;
	private ErrorContainer getAnimationElementsErrorContainer = new ErrorContainer();

	private void finishGraphicsPart(List<AnimationPart> animationPartList,
			ArrayList<GraphicByte> graphicByteList) {
		// do not check if graphicByteList is empty - might be $FF,$FF, - is
		// allowed
		GraphicsPart gp = new GraphicsPart(graphicByteList);
		animationPartList.add(gp);
		graphicByteList.clear();
	}

	private void finishTextPart(List<AnimationPart> animationPartList,
			List<TextElement> textElementList) {
		if (textElementList.isEmpty()) {
			return;
		}
		TextPart tp = new TextPart(textElementList);
		animationPartList.add(tp);
		textElementList.clear();
	}

	private final static String DOLLAR = "$";

	/**
	 * 
	 * @param err
	 * @return
	 */
	private List<AnimationPart> getAnimationElements(
			ErrorContainer outsideErrorContainer) {
		if (animationPartList != null) {
			outsideErrorContainer.addAll(getAnimationElementsErrorContainer);
			return animationPartList;
		}

		animationPartList = new LinkedList<AnimationPart>();

		ErrorContainer err = getAnimationElementsErrorContainer;
		// this is just for shortening the variable
		assert (err.isEmpty());

		List<TextElement> textElementList = new LinkedList<TextElement>();
		ArrayList<GraphicByte> graphicByteList = new ArrayList<GraphicByte>();

		boolean directMode = false;

		// only outside of loop to avoid adding to the stack multiple times
		// (see Java specification)
		TextElement t;
		GraphicByte g;

		loop: for (int i = 0; i < originalRawString.length(); i++) {
			char c = originalRawString.charAt(i);

			if (directMode) { // graphic mode, bytes only
				int dollarPos = i + 1; // for error messages for the user -
				// count from 1 not 0
				if (c != '$') {
					err.addError("Error at position " + dollarPos + ".");
					err.addError("In direct mode (turned on with $FF,) only bytes (starting with $) are allowed, instead of $ found "
							+ c + ".");
					err.addError("Fallback: Automatically adding $FF before "
							+ c + ", interpreting " + c + " as text");
					finishGraphicsPart(animationPartList, graphicByteList);
					directMode = false;
					i--; // c is read again in the next loop, now with
					// directMode=false, i.e. as text
					break loop;
				}

				// (try to) read next three chars
				// 2nd char
				i++;
				int c2Pos = i + 1; // for error messages for the user - count
				// from 1 not 0
				if (i > originalRawString.length() - 1) {
					err.addError("Line ends in incomplete byte.");
					break loop;
				}
				char c2 = Character.toUpperCase(originalRawString.charAt(i));
				// 3rd char
				i++;
				@SuppressWarnings("unused")
				int c3Pos = i + 1; // for error messages for the user - count
				// from 1 not 0
				if (i > originalRawString.length() - 1) {
					err.addError("Line ends in incomplete byte.");
					break loop;
				}
				char c3 = Character.toUpperCase(originalRawString.charAt(i));
				// 4th char
				i++;
				int c4Pos = i + 1; // for error messages for the user - count
				// from 1 not 0
				char c4 = ',';
				if (i > originalRawString.length() - 1) {
					err.addError("Line ends in incomplete byte.");
					err.addError("Do not forget separator, e.g. comma.");
					// do not break loop, just add missing separator
				} else {
					c4 = originalRawString.charAt(i);
				}

				String threeChars = DOLLAR + c2 + c3;
				String fourChars = threeChars + c4;

				boolean isHex = ConversionUtil.isHexSequence(threeChars, err);
				boolean isC4Separator = IniConf.isSeparator(c4);

				if (isHex && !isC4Separator) {
					err.addError(c4
							+ " is not a byte separator char (position: "
							+ c4Pos + ").");
					err.addError("Automatically adding comma as separator.");
					c4 = ',';
					fourChars = threeChars + c4;
					i--; // character will be read in the next round
				}

				if (ByteElement.isDirectModeSwitchByte(fourChars)) {
					directMode = false;
					finishGraphicsPart(animationPartList, graphicByteList);
					continue loop;
				}

				// assert isC4Separator;
				if (isHex) {
					g = new GraphicByte(fourChars, err);
					graphicByteList.add(g);
					continue loop;
				}

				// something is wrong - cannot be interpreted as byte

				// maybe e.g. $3, instead of $03, - c4 is $ in that case
				if (ConversionUtil.isHexDigit(c2) && IniConf.isSeparator(c3)
						&& c4 == '$') {
					String oldThreeChars = threeChars;
					c4 = c3;
					c3 = c2;
					c2 = '0';
					threeChars = DOLLAR + c2 + c3;
					fourChars = threeChars + c4;
					g = new GraphicByte(fourChars, err);
					graphicByteList.add(g);
					i--; // fourth char needs to be read in next round
					err.addError("Bytes must have two digits ($nn,). Inserted a 0 at "
							+ c2Pos
							+ "to make "
							+ oldThreeChars
							+ " into " + threeChars + ".");
					continue loop;
				}

				// final fallback: pretend there was a $FF, for
				// "end of direct mode" before
				finishGraphicsPart(animationPartList, graphicByteList);
				directMode = false;
				err.addError("Inserting $FF, at position " + dollarPos
						+ " to end direct mode because " + threeChars
						+ "cannot be parsed as byte.");
				// the three chars need to be read again (as text) in the next
				// rounds:
				i--;
				i--;
				i--;

			} else { // !directMode, i.e. text or reference

				// normal characters
				if (c != '~' && c != '^' && c != '$') {
					t = new SimpleChar(c);
					textElementList.add(t);
					// the above also worked for illegal chars like é or à,
					// they get displayed as question marks
					if (!t.isValid(err)) {
						// isValid already added the error message to err
						err.addError("Position of the above error: character "
								+ (i + 1)
								+ " of the raw string. (Counting from 1.)");
					}
				}
				// escape characters ~, ^, $
				else {
					i++; // first increase

					// escape character at the end of the line
					if (i > originalRawString.length() - 1) {
						err.addError("Line ends in escape character " + c);
						err.addError("Duplicating "+c);
						t = new EscapeChar(c);
						textElementList.add(t);
						break loop;
					}
					char next = originalRawString.charAt(i);
					// escape for the character itself? I.e. ~~ ^^ $$
					if (next == c) { // Yes, ~~ ^^ $$
						t = new EscapeChar(c);
						textElementList.add(t);
					} else { // No, special (roof) chars or text byte or
								// reference animation
						switch (c) {
						case '^': // (but not ^^) ^A for € etc.
							t = new RoofChar(next);
							textElementList.add(t);
							if (!t.isValid(err)) {
								// isValid already added the error message to
								// err
								err.addError("Position of the above error: character "
										+ (i + 1)
										+ " of the raw string. (Counting from 1.)");
							}
							break; // switch case
						case '$': // (but not $$) text byte (graphic bytes are
									// dealt with further up)
							@SuppressWarnings("unused")
							int dollarPos = i; // previous position for error
												// messages for the user - count
												// from 1 not 0

							// (try to) read next three chars (first one has
							// already been read as "next")
							// second char
							int c2Pos = i + 1; // for error messages - count
												// from 1
							char c2 = next;
							// 3rd char
							i++;
							@SuppressWarnings("unused")
							int c3Pos = i + 1; // for error messages - count
												// from 1
							if (i > originalRawString.length() - 1) {
								err.addError("Line ends in incomplete byte.");
								break loop;
							}
							char c3 = Character.toUpperCase(originalRawString
									.charAt(i));
							// 4th char
							i++;
							int c4Pos = i + 1; // for error messages - count
												// from 1
							char c4 = ',';
							if (i > originalRawString.length() - 1) {
								err.addError("Line ends in incomplete byte.");
								err.addError("Do not forget separator, e.g. comma.");
								// do not break loop, just add missing separator
							} else {
								c4 = originalRawString.charAt(i);
							}

							String threeChars = DOLLAR + c2 + c3;
							String fourChars = threeChars + c4;

							boolean isHex = ConversionUtil.isHexSequence(
									threeChars, err);
							boolean isC4Separator = IniConf.isSeparator(c4);

							if (isHex && !isC4Separator) {
								err.addError(c4
										+ " is not a byte separator char (position: "
										+ c4Pos + ").");
								err.addError("Automatically adding comma as separator.");
								c4 = ',';
								fourChars = threeChars + c4;
								i--; // character will be read in the next round
							}

							if (ByteElement.isDirectModeSwitchByte(fourChars)) {
								directMode = true;
								finishTextPart(animationPartList,
										textElementList);
								continue loop;
							}

							// assert isC4Separator;
							if (isHex) {
								t = new TextByte(fourChars, err);
								textElementList.add(t);
								continue loop;
							}

							// something is wrong - cannot be interpreted as
							// byte

							// maybe e.g. $3, instead of $03,
							if (ConversionUtil.isHexDigit(c2)
									&& IniConf.isSeparator(c3)) {
								String oldThreeChars = threeChars;
								c4 = c3;
								c3 = c2;
								c2 = '0';
								threeChars = DOLLAR + c2 + c3;
								fourChars = threeChars + c4;
								t = new TextByte(fourChars, err);
								textElementList.add(t);
								i--; // fourth char needs to be read in next
										// round
								err.addError("Bytes must have two digits ($nn,). Inserted a 0 at "
										+ c2Pos
										+ "to make "
										+ oldThreeChars
										+ " into " + threeChars + ".");
								continue loop;
							}

							// final fallback: pretend there was a second $
							// (replace $ with $$)
							t = new EscapeChar('$');
							textElementList.add(t);

							// the three chars need to be read again in the next
							// rounds:
							i--;
							i--;
							i--;

							break; // switch case

						case '~':
							finishTextPart(animationPartList, textElementList);
							ReferenceElement referenceElement = new ReferenceElement(
									next, err);
							// this will display invalid or unsupported if non-existing reference
							animationPartList.add(referenceElement);
							break; // switch case
						} // end switch ^ $ ~
					} // if next==c else part
				} // if normal character (not ^ $ ~) else part
			} // end !directMode
		} // end loop over text

		if (directMode) {
			finishGraphicsPart(animationPartList, graphicByteList);
			err.addError("There is an $FF, missing to finish the direct mode at the end of the line.");
			assert (textElementList.isEmpty());
		} else {
			// not an error, normal ending
			if (!textElementList.isEmpty()) { // empty if line ended in
												// reference like ~A
				finishTextPart(animationPartList, textElementList);
			}
		}

		outsideErrorContainer.addAll(err);
		
		modifiedRawString = "";
		for (AnimationPart part : animationPartList) {
			modifiedRawString += part.getRawString();			
		}

		return animationPartList;
	}

	private boolean[][] leds;

	/**
	 * 
	 * @return
	 */
	public boolean[][] getLeds() {
		if (leds != null) {
			return leds;
		}

		int numColumns = 0;
		for (AnimationPart part : animationPartList) {
			numColumns += part.getNumColumns();
		}

		leds = new boolean[numColumns][IniConf.getInstance().rows()];

		int totalColumn = 0;
		for (AnimationPart part : animationPartList) {
			boolean[][] partLeds = part.getData();
			System.arraycopy(partLeds, 0, leds, totalColumn, partLeds.length);
			totalColumn += partLeds.length;
		}

		return leds;
	}

	private boolean[] clickEditable;

	public boolean[] getClickEditableColumns() {
		if (clickEditable != null) {
			return clickEditable;
		}

		int columns = getLeds().length;
		clickEditable = new boolean[columns];

		int column = 0;

		for (AnimationPart part : animationPartList) {
			boolean editable = part.isClickEditable();
			for (int i = 0; i < part.getNumColumns(); i++) {
				this.clickEditable[column] = editable;
			}
		}

		return clickEditable;
	}

	@Override
	public int getNumColumns() {
		return getLeds().length;
	}

	@Override
	public int getNumBytes() {
		int bytes = 0;
		for (AnimationPart part : this
				.getAnimationElements(new ErrorContainer())) {
			bytes += part.getNumBytes();
		}

		return bytes;
	}
}
