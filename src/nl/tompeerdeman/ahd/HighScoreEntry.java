/**
 * File: HighScoreEntry.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import java.io.Serializable;

/**
 * @author Tom Peerdeman
 * 
 */
public class HighScoreEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final HangmanStatus status;
	private final HangmanSettings settings;
	
	/**
	 * @param status
	 * @param settings
	 */
	public HighScoreEntry(HangmanStatus status, HangmanSettings settings) {
		this.status = status;
		this.settings = settings;
	}
	
	/**
	 * @return the status
	 */
	public HangmanStatus getStatus() {
		return status;
	}
	
	/**
	 * @return the settings
	 */
	public HangmanSettings getSettings() {
		return settings;
	}
}
