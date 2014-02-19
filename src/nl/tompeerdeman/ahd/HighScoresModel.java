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
	private static HighScoresModel instance;
	
	private static final long serialVersionUID = 1L;
	
	public final static int MAX_HIGHSCORE_DISPLAY = 10;
	
	protected List<HighScoreEntry> highScoresAll;
	protected List<HighScoreEntry> highScoresEvil;
	protected List<HighScoreEntry> highScoresNormal;
	
	protected boolean loaded;
	
	public static HighScoresModel getInstance() {
		return instance;
	}
	
	protected HighScoresModel() {
		HighScoresModel.instance = this;
		loaded = false;
	}
	
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
	
	public void ensureLoaded() {
		if(!loaded) {
			load();
		}
	}
	
	protected abstract void load();
	
	/**
	 * @param entry
	 * @param evil
	 */
	public abstract void insertNew(HighScoreEntry entry, boolean evil);
	
	/**
	 * @param time
	 * @return An array containing the highscore positions of the 3 highscore
	 *         classes (0: evil, 1: normal, 2: all)
	 */
	public abstract int[] getHighScorePosition(long time);
}
