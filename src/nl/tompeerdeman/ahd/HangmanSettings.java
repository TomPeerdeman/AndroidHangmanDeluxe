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
	
	private final boolean revealSpaces;
	private final boolean evil;
	
	/**
	 * @param maxGuesses
	 * @param maxWordLength
	 * @param minWordLength
	 * @param revealSpaces
	 * @param evil
	 */
	public HangmanSettings(byte maxGuesses, int maxWordLength,
			int minWordLength, boolean revealSpaces, boolean evil) {
		this.maxGuesses = maxGuesses;
		this.maxWordLength = maxWordLength;
		this.minWordLength = minWordLength;
		this.revealSpaces = revealSpaces;
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
	 * @return True if we should reveal spaces, otherwise false
	 */
	public boolean shouldRevealSpaces() {
		return revealSpaces;
	}
	
	/**
	 * @return True if the game is evil, otherwise false
	 */
	public boolean isEvil() {
		return evil;
	}
}
