/*******************************************************************************
 * This program is made available under the terms of the GPLv3 or higher
 * which accompanies it and is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
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
