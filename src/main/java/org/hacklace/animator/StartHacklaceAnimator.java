package org.hacklace.animator;

import org.hacklace.animator.gui.AnimatorGui;

public class StartHacklaceAnimator {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		if (args.length > 0) {
			new AnimatorGui(args[0]);
		} else {
			new AnimatorGui();
		}
	}

}
