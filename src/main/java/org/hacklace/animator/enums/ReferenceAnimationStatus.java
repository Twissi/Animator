package org.hacklace.animator.enums;

public enum ReferenceAnimationStatus {

    OKAY (""), 
    OUT_OF_BOUNDS ("Referenced animation does not exist."), 
    SELF_REFERENCE ("Animation must not refer to itself."), 
    OTHER_REFERENCE ("Animation must not refer to another reference animation.");
    
    private final String errorMessage;

	private ReferenceAnimationStatus(String errorMessage) {
		this.errorMessage = errorMessage;
    }
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	public boolean isValidReference() {
		return this == OKAY;
	}

}
