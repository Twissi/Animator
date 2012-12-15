package org.hacklace.animator.gui;

public interface LedObserver {
	public void onLedChange(int row, int column, boolean newValue);
}
