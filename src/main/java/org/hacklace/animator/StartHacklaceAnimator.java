package org.hacklace.animator;

import org.hacklace.animator.gui.AnimatorGui;

public class StartHacklaceAnimator {

	public static void main(String[] args) {
		if (args.length > 0) {
			AnimatorGui.createInstanceForFilename(args[0]);
		} else {
			AnimatorGui.getInstance();
		}
	}

}
