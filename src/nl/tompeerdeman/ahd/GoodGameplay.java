/**
 * File: GoodGameplay.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

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
