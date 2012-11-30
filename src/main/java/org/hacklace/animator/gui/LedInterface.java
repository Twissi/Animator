package org.hacklace.animator.gui;


public interface LedInterface {
	public void set();

	public void unset();

	public void toggle();
	
	public boolean isOn();
	
	public int getRow();
	
	public int getColumn();

}
