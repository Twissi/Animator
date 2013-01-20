/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.enums;

public enum Direction {
	FORWARD (0),
	BIDIRECTIONAL (1);
	
	public final int value;
	Direction(int value) {
		this.value = value;
	}
	public int getValue() {
		return this.value;
	}
}
