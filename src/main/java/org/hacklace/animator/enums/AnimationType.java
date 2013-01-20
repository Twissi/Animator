/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.enums;

public enum AnimationType {
	TEXT ("Text Animation"), GRAPHIC("Graphic Animation"), REFERENCE("Reference Animation"), MIXED("Mixed Animation");
	
	private String description;

	public String getDescription() {
		return description;
	}

	private AnimationType(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
}
