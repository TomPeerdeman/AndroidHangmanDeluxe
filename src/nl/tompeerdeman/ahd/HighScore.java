/**
 * File: HighScore.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tom Peerdeman
 * 
 */
public class HighScore implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final List<HighScoreEntry> highScores;
	
	/**
	 * @param highScores
	 */
	public HighScore(List<HighScoreEntry> highScores) {
		this.highScores = highScores;
	}
	
	/**
	 * @return the highScores
	 */
	public List<HighScoreEntry> getHighScores() {
		return highScores;
	}
}
