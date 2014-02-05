/**
 * File: HangmanGoodStatus.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

/**
 * @author Tom Peerdeman
 * 
 */
public class HangmanGoodStatus extends HangmanStatus {
	private static final long serialVersionUID = 1L;
	
	private char[] word;
	
	/**
	 * Constructor for loading the status.
	 * 
	 * @param guesses
	 * @param guessedChars
	 * @param word
	 */
	public HangmanGoodStatus(byte guesses, char[] guessedChars, char[] word) {
		super(guesses, guessedChars);
		this.word = word;
	}

	/**
	 * Constructor for new game.
	 * 
	 * @param startNumGuesses
	 * @param word
	 */
	public HangmanGoodStatus(byte startNumGuesses, String word) {
		this(startNumGuesses, word.trim().toCharArray());
	}
	
	/**
	 * @param startNumGuesses
	 * @param word
	 */
	protected HangmanGoodStatus(byte startNumGuesses, char[] word) {
		super(startNumGuesses, (byte) word.length);
		this.word = word;
	}
	
	/**
	 * Reveal all white space.
	 * Used in easy and normal mode.
	 */
	public void revealWhiteSpace() {
		// Find the whitespace's and reveal them
		for(int i = 0; i < word.length; i++) {
			if(word[i] == ' ') {
				reveal(i, ' ');
			}
		}
	}

	/* (non-Javadoc)
	 * @see nl.tompeerdeman.ahd.HangmanStatus#getWordChars()
	 */
	@Override
	public char[] getWordChars() {
		return word;
	}
}
