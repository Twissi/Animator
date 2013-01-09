package org.hacklace.animator.displaybuffer;

public interface Size {
	/**
	 * for the UI, the number of LED columns. Maximum 200 per Animation.
	 * 
	 * @return
	 */
	public int getNumColumns();

	/**
	 * number of bytes, e.g. 2 for a reference, 1 per letter, 1 per byte in
	 * direct mode. Add 1 for modus byte, 1 for 0 delimiter at end of line.
	 * Maximum 256 across all animations.
	 * 
	 * @return
	 */
	public int getNumBytes();
}
