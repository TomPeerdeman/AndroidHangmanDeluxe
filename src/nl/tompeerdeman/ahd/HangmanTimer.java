/**
 * File: HangmanTimer.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import android.os.Handler;
import android.os.SystemClock;

/**
 * @author Tom Peerdeman
 * 
 */
public class HangmanTimer implements Runnable {
	private final MainActivity main;
	private final Handler timerHandler;
	
	private long startTime;
	private boolean running;
	
	/**
	 * @param main
	 */
	public HangmanTimer(MainActivity main) {
		this.main = main;
		timerHandler = new Handler();
		
		running = false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if(!running) {
			return;
		}
		
		main.getGame().getStatus()
			.setTime(SystemClock.elapsedRealtime() - startTime);
		main.showCurrentTime();
		
		timerHandler.postDelayed(this, 100);
	}
	
	/**
	 * Stop the timer. The timer will be ran a last time to update the time in
	 * the status.
	 */
	public void stopTimer() {
		timerHandler.removeCallbacksAndMessages(this);
		run();
		running = false;
	}
	
	/**
	 * Start the timer.
	 * The starting position is based on the time of the status.
	 * For example if the time in the status is at 10 seconds this timer will
	 * start at 10 seconds.
	 */
	public void startTimer() {
		running = true;
		
		startTime =
			SystemClock.elapsedRealtime()
					- main.getGame().getStatus().getTime();
		
		timerHandler.postDelayed(this, 100);
	}
	
	/**
	 * @return True if the timer is running, otherwise false
	 */
	public boolean isRunning() {
		return running;
	}
}
