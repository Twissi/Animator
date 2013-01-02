package org.hacklace.animator;

import static org.hacklace.animator.enums.ErrorType.*;
import java.util.LinkedList;
import java.util.List;

import org.hacklace.animator.enums.ErrorType;

public class ErrorContainer {

	private List<ErrorElement> list;

	public ErrorContainer() {
		list = new LinkedList<ErrorElement>();
	}

	public void addError(String message) {
		addErrorElement(ERROR, message);
	}

	public void addWarning(String message) {
		addErrorElement(WARNING, message);
	}

	public void addSuccessMessage(String message) {
		addErrorElement(SUCCESS, message);
	}

	public void addAbortMessage(String message) {
		addErrorElement(ABORT, message);
	}

	public void addInformationMessage(String message) {
		addErrorElement(INFORMATION, message);
	}

	public void addErrorElement(ErrorType errorType, String message) {
		ErrorElement errorElement = new ErrorElement(errorType, message);
		list.add(errorElement);
	}
	
	public void addErrorElement(String message, ErrorType errorType) {
		addErrorElement(errorType, message);
	}

	public void addErrorElement(ErrorElement errorElement) {
		assert errorElement != null;
		list.add(errorElement);
	}

	public ErrorElement[] getErrorElements() {
		return list.toArray(new ErrorElement[list.size()]);
	}

	public void addAll(ErrorContainer other) {
		assert other != null;
		for (ErrorElement element : other.getErrorElements()) {
			list.add(element);
		}
	}

	public boolean containsFailure() {
		for (ErrorElement element : list) {
			if (element.isFailure()) {
				return true;
			}
		}
		return false;
	}

	public void clear() {
		list.clear();
	}

}
