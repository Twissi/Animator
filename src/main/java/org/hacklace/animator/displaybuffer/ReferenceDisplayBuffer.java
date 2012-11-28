package org.hacklace.animator.displaybuffer;

import java.util.List;

import org.hacklace.animator.enums.AnimationType;

public class ReferenceDisplayBuffer extends DisplayBuffer {

	private char letter;

	private List<DisplayBuffer> list;

	public ReferenceDisplayBuffer(char letter, List<DisplayBuffer> list) {
		this.letter = letter;
		this.list = list;
	}

	private DisplayBuffer getReferencedDisplayBuffer() {
		int index = letter - 'A';
		DisplayBuffer otherDisplayBuffer = this.list.get(index);
		return otherDisplayBuffer;
	}

	@Override
	public int getStepWidth() {
		return getReferencedDisplayBuffer().getStepWidth();
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
	public void setLetter(char letter) {
		this.letter = letter;
	}
	@Override
	public boolean getValueAt(int x, int y) {
		return getReferencedDisplayBuffer().data[x][y];
	}
	@Override
	public void rewind() {
		getReferencedDisplayBuffer().rewind();
	}
	@Override
	public void moveLeft() {
		getReferencedDisplayBuffer().moveLeft();
	}
	@Override
	public void moveRight() {
		getReferencedDisplayBuffer().moveRight();
	}
	@Override
	public Grid getPrevious() {
		return getReferencedDisplayBuffer().getPrevious();
	}
	@Override
	public Grid getCurrent() {
		return getReferencedDisplayBuffer().getCurrent();
	}
	@Override
	public Grid getNext() {
		return getReferencedDisplayBuffer().getNext();
	}

}
