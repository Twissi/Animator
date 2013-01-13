package org.hacklace.animator.gui.actions;

public interface LedObserver {
	public void onLedChange(int column, int row, boolean newValue);
}
