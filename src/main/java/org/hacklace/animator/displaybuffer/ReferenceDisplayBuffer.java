package org.hacklace.animator.displaybuffer;

import java.util.List;

import org.hacklace.animator.enums.AnimationType;
import org.hacklace.animator.enums.ReferenceAnimationStatus;

public class ReferenceDisplayBuffer extends DisplayBuffer {

	private char letter;

	private List<DisplayBuffer> list;

	private ReferenceAnimationStatus errorStatus;

	public ReferenceDisplayBuffer(char letter, List<DisplayBuffer> list)
			throws IllegalAnimationReferenceException {
		this.letter = letter;
		this.list = list;
		this.errorStatus = ReferenceAnimationStatus.OKAY;
		// trigger checks:
		getReferencedDisplayBuffer();
	}

	private DisplayBuffer getReferencedDisplayBuffer()
			throws IllegalAnimationReferenceException {
		int index = letter - 'A';
		if (index >= this.list.size()) {
			this.errorStatus = ReferenceAnimationStatus.OUT_OF_BOUNDS;
			throw new IllegalAnimationReferenceException(errorStatus.getErrorMessage());
		}
		DisplayBuffer otherDisplayBuffer = this.list.get(index);
		if (otherDisplayBuffer == this) {
			this.errorStatus = ReferenceAnimationStatus.SELF_REFERENCE;
			// absolutely essential to prevent infinite loops!
			throw new IllegalAnimationReferenceException(errorStatus.getErrorMessage());
		}
		if (otherDisplayBuffer.isReferenceBuffer()) {
			this.errorStatus = ReferenceAnimationStatus.OTHER_REFERENCE;
			throw new IllegalAnimationReferenceException(errorStatus.getErrorMessage());
		}
		return otherDisplayBuffer;
	}
	
	public boolean isValidReference() {
		try {
			getReferencedDisplayBuffer();
		} catch (IllegalAnimationReferenceException e) {
			return false;
		}
		return true;
	}
	
	public ReferenceAnimationStatus getReferenceErrorStatus() {
		try {
			getReferencedDisplayBuffer();
		} catch (IllegalAnimationReferenceException ex) {
			// do nothing
		}
		return this.errorStatus;
	}

	@Override
	public int getStepWidth() {
		try {
			return getReferencedDisplayBuffer().getStepWidth();
		} catch (IllegalAnimationReferenceException ex) {
			return 5;
		}
	}

	@Override
	public AnimationType getAnimationType() {
		return AnimationType.GRAPHIC;
	}

	@Override
	public boolean isReferenceBuffer() {
		return true;
	}

	public char getLetter() {
		return this.letter;
	}
	public void setLetter(char letter) throws IllegalAnimationReferenceException {
		this.letter = letter;
		// trigger checks:
		getReferencedDisplayBuffer();		
	}
	@Override
	public boolean getValueAt(int x, int y) {
		try {
			return getReferencedDisplayBuffer().data[x][y];
		} catch (IllegalAnimationReferenceException e) {
			return false;
		}
	}

	@Override
	public void moveLeft() {
		try {
			getReferencedDisplayBuffer().moveLeft();
		} catch (IllegalAnimationReferenceException e) {
			super.moveLeft();
		}
	}
	@Override
	public void moveRight() {
		try {
			getReferencedDisplayBuffer().moveRight();
		} catch (IllegalAnimationReferenceException ex) {
			super.moveRight();
		}
	}
	@Override
	public Grid getPrevious() {
		try {
			return getReferencedDisplayBuffer().getPrevious();
		} catch (IllegalAnimationReferenceException ex) {
			return new Grid();
		}
	}
	@Override
	public Grid getCurrent() {
		try {
			return getReferencedDisplayBuffer().getCurrent();
		} catch (IllegalAnimationReferenceException e) {
			return new Grid();
		}
	}
	@Override
	public Grid getNext() {
		try {
			return getReferencedDisplayBuffer().getNext();
		} catch (IllegalAnimationReferenceException e) {
			return new Grid();
		}
	}

	@Override
	public String toString() {
		return "Animations-Referenz" + " ~" + letter + " " + this.errorStatus.getErrorMessage();
	}
	
	@Override
	public boolean getColumnRow(int column, int row) {
		try {
			return getReferencedDisplayBuffer().data[column][row];
		} catch (IllegalAnimationReferenceException e) {
			return false;
		}
	}

}
