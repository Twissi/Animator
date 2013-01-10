package org.hacklace.animator.gui;

public interface LedObserver {
	public void onLedChange(int column, int row, boolean newValue);
}
