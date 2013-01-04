package org.hacklace.animator.configuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hacklace.animator.ConversionUtil;
import org.hacklace.animator.ErrorContainer;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.displaybuffer.AnimationPart;
import org.hacklace.animator.displaybuffer.ByteElement;
import org.hacklace.animator.displaybuffer.EscapeChar;
import org.hacklace.animator.displaybuffer.GraphicByte;
import org.hacklace.animator.displaybuffer.GraphicsPart;
import org.hacklace.animator.displaybuffer.ReferenceElement;
import org.hacklace.animator.displaybuffer.RoofChar;
import org.hacklace.animator.displaybuffer.SimpleChar;
import org.hacklace.animator.displaybuffer.Size;
import org.hacklace.animator.displaybuffer.TextByte;
import org.hacklace.animator.displaybuffer.TextElement;
import org.hacklace.animator.displaybuffer.TextPart;
import org.hacklace.animator.enums.AnimationType;

/**
 * Hacklace configuration line without modus byte. Immutable
 * 
 * @author monika
 * 
 */

public class RestOfConfigLine implements Size {

	private String restOfLineString;
	public final static String DIRECT = "$FF";

	/**
	 * 
	 * @param restOfLineString
	 *            may be invalid (but not null)
	 */
	public RestOfConfigLine(String restOfLineString) {
		assert (restOfLineString != null);
		this.restOfLineString = restOfLineString;
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
		while ((index = restOfLineString.indexOf(DIRECT, index)) != -1) {
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
		if (!restOfLineString.startsWith(DIRECT))
			return false;
		int length = restOfLineString.length();
		if (length < 4)
			return false;
		String end = restOfLineString.substring(length - 4, length - 1);
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
		if (restOfLineString.trim().length() < 2)
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
		return restOfLineString.startsWith("~")
				&& restOfLineString.length() == 2
				&& restOfLineString.charAt(1) != '~';
	}

	/**
	 * 
	 * @param line
	 *            can be rest of line (without modus byte) or full line (with
	 *            modus byte)
	 * @return
	 */
	boolean containsReference() {
		if (restOfLineString == null)
			return false;
		if (restOfLineString.length() < 2)
			return false;
		if (!restOfLineString.contains("~"))
			return false;
		// line now certainly contains ~
		char last = ' ';
		for (char c : restOfLineString.toCharArray()) {
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

	public String getValue() {
		return restOfLineString;
	}

	@Override
	public String toString() {
		return restOfLineString;
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
	private ErrorContainer getAnimationElementsErrorContainer = null;

	/**
	 * 
	 * Do not modify the returned error container.
	 * 
	 * @return
	 */
	public ErrorContainer getErrors() {
		if (getAnimationElementsErrorContainer == null) {
			getAnimationElements(new ErrorContainer());
		}
		return getAnimationElementsErrorContainer;
	}

	/**
	 * 
	 * @param errorContainer
	 *            should be empty, will be cleared, must not be modified later
	 *            (outside) as a local copy is kept
	 * @return
	 */
	private List<AnimationPart> getAnimationElements(
			ErrorContainer errorContainer) {
		if (animationPartList != null) {
			errorContainer.clear();
			errorContainer.addAll(this.getAnimationElementsErrorContainer);
			return animationPartList;
		}

		errorContainer.clear();
		this.getAnimationElementsErrorContainer = errorContainer;

		animationPartList = new LinkedList<AnimationPart>();

		List<TextElement> textElementList = new LinkedList<TextElement>();
		ArrayList<GraphicByte> graphicByteList = new ArrayList<GraphicByte>();

		boolean directMode = false;

		// only outside of loop to avoid adding to the stack multiple times
		TextElement t;
		GraphicByte g;

		loop: for (int i = 0; i < restOfLineString.length(); i++) {
			char c = restOfLineString.charAt(i);

			if (directMode && c != '$') {
				errorContainer
						.addError(c
								+ " inside direct mode not allowed (close with \"$FF,\").");
				// fallback - pretend there was a $FF, before
				directMode = false;
				// (do not check whether graphicByteList is empty)
				GraphicsPart graphicsPart = new GraphicsPart(graphicByteList);
				animationPartList.add(graphicsPart);
				graphicByteList.clear();

			}

			// normal characters
			if (c != '~' && c != '^' && c != '$') {
				t = new SimpleChar(c);
				textElementList.add(t);
			}
			// escape characters ~, ^, $
			else {
				i++; // first increase

				// escape character at the end of the line
				if (i > restOfLineString.length() - 1) {
					errorContainer.addError("Line ends in escape character "
							+ c);
					break loop;
				}
				char next = restOfLineString.charAt(i);
				// escape for the character itself? I.e. ~~ ^^ $$
				if (next == c) { // Yes, ~~ ^^ $$
					t = new EscapeChar(c);
					textElementList.add(t);
				} else { // No, special (roof) chars or reference animation
					switch (c) {
					case '^': // (but not ^^) ^A for € etc.
						t = new RoofChar(c);
						textElementList.add(t);
						break; // switch case
					case '$': // (but not $$) text or graphic
						if (ConversionUtil.isHexDigit(next)) {
							errorContainer.addError(next+" is following $ but is not a valid hex digit (0-9A-F in upper case).");
							next = '2'; // 0x20 is the minimum text byte and also allowed for graphics
						}						
						String fourChars = "$" + next;
						i++; // second increase
						// ignore if end of string
						if (i > restOfLineString.length() - 1) {
							errorContainer
									.addError("Line ends in incomplete byte "
											+ fourChars);
							break loop;
						}
						char c3 = restOfLineString.charAt(i);
						if (ConversionUtil.isHexDigit(c3)) {
							errorContainer.addError("$"+next+c3+" is not a valid hex sequence (only 0-9A-F in upper case allowed).");
							c3 = '0'; // replace the invalid char with an allowed one
						}
						
						fourChars += restOfLineString.charAt(i);
						i++; // third increase
						if (i > restOfLineString.length() - 1) {
							errorContainer
									.addError("Line ends in incomplete byte "
											+ fourChars+". (Do not forget final separator, e.g. a comma.)");
							break loop;
						}
						char c4 = restOfLineString.charAt(i);
						if (IniConf.isSeparator(c4)) {
							errorContainer.addWarning(c4+" (in "+fourChars+") is not a typical separator. You probably forgot to put one (e.g. comma or space).");
						}						
						fourChars += c4;

						if (ByteElement.isDirectMode(fourChars)) {
							directMode = !directMode;
							if (directMode && !textElementList.isEmpty()) {
								// could be empty if last was reference
								TextPart textPart = new TextPart(
										textElementList);
								animationPartList.add(textPart);
								textElementList.clear();
							} else { // do not check if it is empty
								GraphicsPart graphicsPart = new GraphicsPart(
										graphicByteList);
								animationPartList.add(graphicsPart);
								graphicByteList.clear();
							}
							continue loop;
						}

						if (!directMode) {
							t = new TextByte(fourChars, errorContainer);
							textElementList.add(t);
						} else {
							g = new GraphicByte(fourChars, errorContainer);
							graphicByteList.add(g);
						}
						break; // switch case

					case '~':
						if (!textElementList.isEmpty()) {
							TextPart textPart = new TextPart(textElementList);
							animationPartList.add(textPart);
							textElementList.clear();
						}
						ReferenceElement referenceElement = new ReferenceElement(
								next);
						animationPartList.add(referenceElement);
						break; // switch case
					} // end switch ^ $ ~
				} // if next==c else part
			} // if normal character (not ^ $ ~) else part

		} // end loop over text
		return animationPartList;
	}

	private boolean[][] leds;

	/**
	 * 
	 * @param errorContainer
	 *            should be empty, will be cleared, must not be modified later
	 *            as a local reference is kept
	 * @return
	 */
	public boolean[][] getLeds(ErrorContainer errorContainer) {
		errorContainer.clear();
		if (leds != null) {
			errorContainer.addAll(this.getAnimationElementsErrorContainer);
			return leds;
		}

		List<AnimationPart> list = getAnimationElements(errorContainer);
		int numColumns = 0;
		for (AnimationPart part : list) {
			numColumns += part.getNumColumns();
		}

		leds = new boolean[numColumns][IniConf.getInstance().rows()];

		int totalColumn = 0;
		for (AnimationPart part : list) {
			boolean[][] partLeds = part.getData();
			System.arraycopy(partLeds, 0, leds, totalColumn, partLeds.length);
			totalColumn += partLeds.length;
		}

		return leds;
	}

	private boolean[] clickEditable;

	public boolean[] getClickEditableColumns(ErrorContainer errorContainer) {
		if (clickEditable != null) {

			return clickEditable;
		}

		int columns = getLeds(errorContainer).length;
		clickEditable = new boolean[columns];

		List<AnimationPart> list = getAnimationElements(errorContainer);

		int column = 0;

		for (AnimationPart part : list) {
			boolean editable = part.isClickEditable();
			for (int i = 0; i < part.getNumColumns(); i++) {
				this.clickEditable[column] = editable;
			}
		}

		return clickEditable;
	}

	@Override
	public int getNumColumns() {
		return getLeds(new ErrorContainer()).length;
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
