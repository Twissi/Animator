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
}
