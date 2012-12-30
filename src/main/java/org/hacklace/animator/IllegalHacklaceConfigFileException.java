package org.hacklace.animator;

public class IllegalHacklaceConfigFileException extends Exception {

	private static final long serialVersionUID = 5309534613236859754L;
	
	private IllegalHacklaceConfigLineException causingHacklaceConfigLineException;
	
	private int lineNumber;
	

	public IllegalHacklaceConfigFileException(IllegalHacklaceConfigLineException cause, int lineNumber) {
		super(cause);
		this.causingHacklaceConfigLineException = cause;
		this.lineNumber = lineNumber;
	}

	@Override
	public String getMessage() {
		return this.causingHacklaceConfigLineException.getMessage() + " (Line: "+lineNumber+")";
	}

}
