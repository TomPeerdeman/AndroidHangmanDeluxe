package nl.tompeerdeman.ahd;

import java.io.Serializable;

/**
 * @author Tom Peerdeman
 * 
 */
public class HangmanSettings implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final byte maxGuesses;
	private final int maxWordLength;
	private final int minWordLength;
	
	private final boolean revealNonAlpha;
	private final boolean evil;
	
	/**
	 * @param maxGuesses
	 * @param maxWordLength
	 * @param minWordLength
	 * @param revealNonAlpha
	 * @param evil
	 */
	public HangmanSettings(byte maxGuesses, int maxWordLength,
			int minWordLength, boolean revealNonAlpha, boolean evil) {
		this.maxGuesses = maxGuesses;
		this.maxWordLength = maxWordLength;
		this.minWordLength = minWordLength;
		this.revealNonAlpha = revealNonAlpha;
		this.evil = evil;
	}
	
	/**
	 * @return The maximum amount of guesses
	 */
	public byte getMaxNumGuesses() {
		return maxGuesses;
	}
	
	/**
	 * @return the maxWordLength
	 */
	public int getMaxWordLength() {
		return maxWordLength;
	}
	
	/**
	 * @return the minWordLength
	 */
	public int getMinWordLength() {
		return minWordLength;
	}
	
	/**
	 * @return True if we should non alphabetic spaces, otherwise false
	 */
	public boolean shouldRevealNonAlpha() {
		return revealNonAlpha;
	}
	
	/**
	 * @return True if the game is evil, otherwise false
	 */
	public boolean isEvil() {
		return evil;
	}
}
