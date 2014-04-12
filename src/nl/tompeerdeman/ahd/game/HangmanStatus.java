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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Tom Peerdeman
 * 
 */
public abstract class HangmanStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected byte guesses;
	protected char[] guessedChars;
	
	protected final char[] prevGuessedChars;
	protected int nPrevGuessedChars;
	
	protected long time;
	
	protected transient byte numRevealed;
	
	/**
	 * Constructor for new game.
	 * 
	 * @param startNumGuesses
	 *            The maximum amount of bad guesses
	 * @param numCharacters
	 *            The number of characters in the to be guessed word including
	 *            spaces
	 */
	public HangmanStatus(byte startNumGuesses, byte numCharacters) {
		guesses = startNumGuesses;
		guessedChars = new char[numCharacters];
		numRevealed = 0;
		
		for(int i = 0; i < numCharacters; i++) {
			guessedChars[i] = GameplayDelegate.CHARACTER_UNKNOWN;
		}
		
		prevGuessedChars = new char[26];
		nPrevGuessedChars = 0;
	}
	
	/**
	 * Constructor for loading the status.
	 * 
	 * @param guesses
	 * @param guessedChars
	 */
	public HangmanStatus(byte guesses, char[] guessedChars) {
		this.guesses = guesses;
		this.guessedChars = guessedChars;
		
		prevGuessedChars = new char[26];
		nPrevGuessedChars = 0;
		
		onLoad();
	}
	
	/**
	 * Deserialization. Read the object and calculate the number of revealed
	 * characters.
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		onLoad();
	}
	
	/**
	 * Calculate the number of revealed characters.
	 */
	protected void onLoad() {
		numRevealed = 0;
		for(int i = 0; i < guessedChars.length; i++) {
			if(guessedChars[i] != GameplayDelegate.CHARACTER_UNKNOWN) {
				numRevealed++;
			}
		}
	}
	
	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}
	
	/**
	 * @return True of the game has been lost, otherwise false
	 */
	public boolean hasLostGame() {
		return guesses <= 0;
	}
	
	/**
	 * @return True of the game has been won, otherwise false
	 */
	public boolean hasWonGame() {
		return numRevealed == guessedChars.length;
	}
	
	/**
	 * @return The amount of remaining guesses
	 */
	public byte getRemainingGuesses() {
		return guesses;
	}
	
	/**
	 * @return The characters to display
	 */
	public char[] getGuessedChars() {
		return guessedChars;
	}
	
	/**
	 * @return The characters of the to be guessed word
	 */
	public abstract char[] getWordChars();
	
	/**
	 * Decrement the amount of guesses left.
	 */
	public void decrementGuesses() {
		guesses--;
	}
	
	/**
	 * Reveal a character
	 * 
	 * @param idx
	 *            The position of the character
	 * @param character
	 */
	public void reveal(int idx, char character) {
		if(idx < 0 || idx >= guessedChars.length) {
			throw new IllegalArgumentException("Index out of bounds " + idx
					+ "/" + guessedChars.length);
		}
		
		guessedChars[idx] = character;
		numRevealed++;
	}
	
	/**
	 * Add a character that should be added to the list containing characters
	 * that have already been guessed.
	 * 
	 * @param c
	 */
	public void addPrevGuessedChar(char c) {
		prevGuessedChars[nPrevGuessedChars++] = c;
	}
	
	/**
	 * Check if the supplied character is already guessed in an previous guess.
	 * 
	 * @param c
	 * @return True if the character has already been guessed before, otherwise
	 *         false
	 */
	public boolean isPrevGuessedChar(char c) {
		for(int i = 0; i < nPrevGuessedChars; i++) {
			if(prevGuessedChars[i] == c) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HangmanStatus [guesses=" + guesses + ", guessedChars="
				+ Arrays.toString(guessedChars) + ", time=" + time
				+ ", numRevealed=" + numRevealed + "]";
	}
}
