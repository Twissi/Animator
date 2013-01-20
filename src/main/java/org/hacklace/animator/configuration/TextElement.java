/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator.configuration;

import org.hacklace.animator.ErrorContainer;

public abstract class TextElement {
	
	public abstract int[] getAnimationBytes();
	
	public abstract String getRawString();
	
	public abstract boolean isValid(ErrorContainer errorContainer);
	
	public abstract int getNumColumns();

	@SuppressWarnings("static-method")
	public final int getNumBytes() {
		return 1;
	}

}
