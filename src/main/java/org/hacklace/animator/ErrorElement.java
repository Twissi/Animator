/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package org.hacklace.animator;

import org.hacklace.animator.enums.ErrorType;

public class ErrorElement {

	private final ErrorType type;
	private final String message;

	public ErrorElement(ErrorType type, String message) {
		assert type != null;
		assert message != null;
		assert message.length() != 0;
		this.type = type;
		this.message = message;
	}

	public ErrorType getType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean isFailure() {
		return type.isFailure();
	}
	
	public boolean isErrorOrWarning() {
		return type.isErrorOrWarning();
	}

	@Override
	public String toString() {
		return "["+getType().getDescription()+"] "+getMessage();
	}
}
