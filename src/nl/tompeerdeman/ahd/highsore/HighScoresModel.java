/*******************************************************************************
 * Copyright (c) 2014 Tom Peerdeman.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Tom Peerdeman - initial API and implementation
 ******************************************************************************/
/**
 * File: HighScore.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.highsore;

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
	
	/**
	 * Ensure the highscores are loaded.
	 * If they are not load them.
	 */
	public void ensureLoaded() {
		if(!loaded) {
			load();
		}
	}
	
	/**
	 * Load the highscores.
	 */
	protected abstract void load();
	
	/**
	 * Insert a new entry into the highscore.
	 * 
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
