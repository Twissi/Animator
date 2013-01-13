package org.hacklace.animator.gui;

import org.hacklace.animator.displaybuffer.DisplayBuffer;
import org.hacklace.animator.enums.Direction;

public class AnimatorRunnable implements Runnable {
	private boolean interrupted = false;
	private DisplayBuffer buffer;
	private LedPanel ledPanel;

	public AnimatorRunnable(DisplayBuffer buffer, LedPanel panel) {
		this.buffer = buffer;
		this.ledPanel = panel;
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ex) {
			interrupted = true;
		}
	}

	@Override
	public void run() {
		/**
		 * Note: The values for delay and speed in the hacklace represent
		 * interrupt ticks. The timer interrupt fires once every 256 oscillator
		 * ticks and is prescaled by 1024. The oscillator is configured at 4
		 * MHz. ser_clk_correction is used here too because the actual hacklace
		 * won't run at exactly 4MHz -> 1 tick = 4.000.000 / 1024 / 256 ms = ~15
		 * ms
		 */
		double tick = 4000000 / 1024 / 256 / AnimatorGui.getIniConf()
				.ser_clk_correction();
		int playPosition = 0;
		boolean playForward = true;
		while (!interrupted && buffer != null) {
			// note: these are all inside the loop so the user can edit the
			// values while playing
			int intSpeed = buffer.getSpeed().getValue();
			int speedSleepTime = (int) ((double) AnimatorGui.getIniConf()
					.speedList().get(intSpeed) * tick);
			int intDelay = buffer.getDelay().getValue();
			int delaySleepTime = (int) ((double) AnimatorGui.getIniConf()
					.delayList().get(intDelay) * speedSleepTime);
			int animationLength = (buffer.getNumColumns());
			int intStepWidth = buffer.getStepWidth().getValue();
			if (playPosition > animationLength - ledPanel.getNumCols())
				playPosition = animationLength - ledPanel.getNumCols();
			if (playPosition < 0)
				playPosition = 0;
			for (int x = 0; x < ledPanel.getNumCols(); x++) {
				for (int y = 0; y < ledPanel.getNumRows(); y++) {
					ledPanel.setLedFromBuffer(x, y,
							buffer.getValueAtColumnRow(x + playPosition, y));
				}
			}
			ledPanel.repaint(); // prevents flickering on slow PCs
			sleep(speedSleepTime);
			if (playForward) {
				playPosition += intStepWidth;
			} else {
				playPosition -= intStepWidth;
			}
			// turn around?
			if (playPosition < 0) {
				if (buffer.getDirection() == Direction.BIDIRECTIONAL) {
					playForward = true;
				}
				sleep(delaySleepTime);
			} else if (playPosition > animationLength - ledPanel.getNumCols()) {
				if (buffer.getDirection() == Direction.BIDIRECTIONAL) {
					playForward = false;
				} else {
					playPosition = 0;
				}
				sleep(delaySleepTime);
			}
		}
	}
}
