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
package nl.tompeerdeman.ahd.game;


/**
 * @author Tom Peerdeman
 * 
 */
public interface GameplayDelegate {
	public static final char CHARACTER_UNKNOWN = '_';
	
	/**
	 * Process a guess.
	 * 
	 * @param settings
	 *            The settings of the game
	 * @param status
	 *            The current status of the hangman game
	 * @param wordDatabase
	 *            The model that provides the words
	 * @param guess
	 *            The guessed character
	 * @return The new status of the game
	 */
	public HangmanStatus onGuess(HangmanSettings settings,
			HangmanStatus status, WordsModel wordDatabase, char guess);
	
	/**
	 * Initialize the status of the game.
	 * 
	 * @param settings
	 *            The settings of the game
	 * @param wordDatabase
	 *            The model that provides the words
	 * @return The new status of the game
	 */
	public HangmanStatus initialize(HangmanSettings settings,
			WordsModel wordDatabase);
}
