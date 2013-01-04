package org.hacklace.animator.enums;

public enum ErrorType {
	ERROR('E', "Error"), //
	WARNING('W', "Warning"), //
	SUCCESS('S', "Success"), //
	ABORT('A', "Abort"), //
	INFORMATION('I', "INFORMATION");

	private final char letter;
	private final String description;

	private ErrorType(char letter, String description) {
		this.letter = letter;
		this.description = description;

	}

	public char getLetter() {
		return letter;
	}

	public String getDescription() {
		return description;
	}

	public boolean isFailure() {
		return this == ERROR || this == ABORT;
	}

	public boolean isErrorOrWarning() {
		return isFailure() || this == WARNING;
	}

}
