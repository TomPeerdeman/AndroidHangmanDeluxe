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
 * File: GoodGameplay.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.game.good;

import nl.tompeerdeman.ahd.game.GameplayDelegate;
import nl.tompeerdeman.ahd.game.HangmanGame;
import nl.tompeerdeman.ahd.game.HangmanSettings;
import nl.tompeerdeman.ahd.game.HangmanStatus;
import nl.tompeerdeman.ahd.game.WordsModel;

/**
 * @author Tom Peerdeman
 * 
 */
public class GoodGameplay implements GameplayDelegate {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.tompeerdeman.ahd.GameplayDelegate#onGuess(nl.tompeerdeman.ahd
	 * .HangmanSettings, nl.tompeerdeman.ahd.HangmanStatus, char)
	 */
	@Override
	public HangmanStatus onGuess(HangmanSettings settings,
			HangmanStatus status, WordsModel wordDatabase, char guess) {
		if(!HangmanGame.validChar(guess)) {
			throw new IllegalArgumentException("Guessed char not in a-z range");
		}
		
		if(status.isPrevGuessedChar(guess)) {
			return status;
		}
		
		char[] word = status.getWordChars();
		char[] revealed = status.getGuessedChars();
		
		boolean charFound = false;
		
		for(int i = 0; i < word.length; i++) {
			// Already guessed and it was already revealed.
			if(revealed[i] == guess) {
				break;
			}
			
			if(word[i] == guess) {
				charFound = true;
				status.reveal(i, guess);
			}
		}
		
		// Did not found the guessed character.
		if(!charFound) {
			status.decrementGuesses();
		}
		
		status.addPrevGuessedChar(guess);
		
		return status;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.tompeerdeman.ahd.GameplayDelegate#initialize(nl.tompeerdeman.ahd
	 * .HangmanSettings, nl.tompeerdeman.ahd.HangmanStatus)
	 */
	@Override
	public HangmanStatus initialize(HangmanSettings settings,
			WordsModel wordDatabase) {
		if(settings.isEvil()) {
			throw new IllegalArgumentException(
					"Initialize nice gameplay but settings say evil gameplay");
		}
		
		// Grab a random word.
		String word =
			wordDatabase.getRandWordInLengthRange(settings.getMinWordLength(),
					settings.getMaxWordLength());
		
		// Create a new nice status using a random word from the model.
		HangmanGoodStatus status =
			new HangmanGoodStatus(settings.getMaxNumGuesses(), word);
		
		// Reveal spaces if the settings say so.
		if(settings.shouldRevealNonAlpha()) {
			status.revealNonAlpha();
		}
		
		return status;
	}
}
