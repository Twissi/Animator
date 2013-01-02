package org.hacklace.animator.displaybuffer;

public interface Size {
  public int getNumColumns(); // for the UI, especially for mixed buffers
  public int getNumBytes(); // reference animations only need 2 bytes, not number of columns; also: add 0 delimiter
}
