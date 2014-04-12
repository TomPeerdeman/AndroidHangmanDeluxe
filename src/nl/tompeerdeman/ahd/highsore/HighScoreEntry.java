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
 * File: HighScoreEntry.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.highsore;

import java.io.Serializable;

import nl.tompeerdeman.ahd.game.HangmanGame;

/**
 * @author Tom Peerdeman
 * 
 */
public class HighScoreEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String word;
	private final int badGuesses;
	private final long time;
	
	/**
	 * Construct a highscore entry from the active game
	 * 
	 * @param game
	 */
	public HighScoreEntry(HangmanGame game) {
		word = new String(game.getStatus().getWordChars());
		badGuesses =
			game.getSettings().getMaxNumGuesses()
					- game.getStatus().getRemainingGuesses();
		time = game.getStatus().getTime();
	}
	
	/**
	 * Construct a highscore entry from previous saved games
	 * 
	 * @param word
	 * @param badGuesses
	 * @param time
	 */
	public HighScoreEntry(String word, int badGuesses, long time) {
		this.word = word;
		this.badGuesses = badGuesses;
		this.time = time;
	}
	
	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * @return the badGuesses
	 */
	public int getBadGuesses() {
		return badGuesses;
	}
	
	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}
}
