/**
 * File: GoodGameplay.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Tom Peerdeman
 * 
 */
public class GoodGameplay extends AbstractGameplayDelegate {
	/**
	 * @param wordDatabase
	 * @param rand
	 */
	public GoodGameplay(WordDatabase wordDatabase, Random rand) {
		super(wordDatabase, rand);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.tompeerdeman.ahd.GameplayDelegate#onGuess(nl.tompeerdeman.ahd
	 * .HangmanSettings, nl.tompeerdeman.ahd.HangmanStatus, char)
	 */
	@Override
	public HangmanStatus onGuess(HangmanSettings settings,
			HangmanStatus status, char guess) {
		if(!validChar(guess)) {
			throw new IllegalArgumentException("Guessed char not in a-z range");
		}
		
		char[] word = status.getWordChars();
		char[] revealed = status.getGuessedChars();
		
		boolean charFound = false;
		
		for(int i = 0; i < word.length; i++) {
			// Already guessed and it was already revealed.
			if(revealed[i] == guess) {
				break;
			}
			
			// The guessed character is in the word.
			if(word[i] == guess) {
				charFound = true;
				status.reveal(i, guess);
			}
		}
		
		// Did not found the guessed character or already guessed this one.
		if(!charFound) {
			status.decrementGuesses();
		}
		
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
	public HangmanStatus initialize(HangmanSettings settings) {
		if(settings.isEvil()) {
			throw new IllegalArgumentException(
					"Initialize nice gameplay but settings say evil gameplay");
		}
		
		// Grab all the words.
		List<String> words =
			wordDatabase.getWordsInLength(settings.getMaxWordLength(),
					settings.getMinWordLength());
		Collections.shuffle(words);
		
		String word;
		if(words.size() > 1) {
			word = words.get(rand.nextInt(words.size() - 1));
		} else {
			word = words.get(0);
		}
		
		// Create a new nice status using a random word from the list.
		HangmanGoodStatus status =
			new HangmanGoodStatus(settings.getMaxNumGuesses(), word);
		
		// Reveal spaces if the settings say so.
		if(settings.shouldRevealSpaces()) {
			status.revealWhiteSpace();
		}
		
		return status;
	}
}
