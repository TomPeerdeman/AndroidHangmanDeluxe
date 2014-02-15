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
public abstract class HighScoresModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected List<HighScoreEntry> highScoresAll;
	protected List<HighScoreEntry> highScoresEvil;
	protected List<HighScoreEntry> highScoresNormal;
	
	/**
	 * @return the highScoresAll
	 */
	public List<HighScoreEntry> getHighScoresAll() {
		return highScoresAll;
	}
	
	/**
	 * @return the highScoresEvil
	 */
	public List<HighScoreEntry> getHighScoresEvil() {
		return highScoresEvil;
	}
	
	/**
	 * @return the highScoresNormal
	 */
	public List<HighScoreEntry> getHighScoresNormal() {
		return highScoresNormal;
	}
	
	/**
	 * @param entry
	 * @param evil
	 */
	public abstract void insertNew(HighScoreEntry entry, boolean evil);
}
