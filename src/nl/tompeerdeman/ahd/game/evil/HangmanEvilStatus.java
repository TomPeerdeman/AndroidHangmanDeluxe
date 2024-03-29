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
 * File: HangmanEvilStatus.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.game.evil;

import java.io.IOException;

import nl.tompeerdeman.ahd.game.HangmanStatus;

/**
 * @author Tom Peerdeman
 * 
 */
public class HangmanEvilStatus extends HangmanStatus {
	private static final long serialVersionUID = 1L;
	
	private char[] equivalenceClass;
	private boolean wordChosen;
	
	/**
	 * Constructor for loading the status.
	 * 
	 * @param guesses
	 * @param guessedChars
	 * @param equivalenceClass
	 * @param wordChosen
	 */
	public HangmanEvilStatus(byte guesses, char[] guessedChars,
			char[] equivalenceClass, boolean wordChosen) {
		super(guesses, guessedChars);
		this.equivalenceClass = equivalenceClass;
		this.wordChosen = wordChosen;
	}
	
	/**
	 * Constructor for new game.
	 * 
	 * @param startNumGuesses
	 * @param equivalenceClass
	 */
	public HangmanEvilStatus(byte startNumGuesses, char[] equivalenceClass) {
		super(startNumGuesses, (byte) equivalenceClass.length);
		this.equivalenceClass = equivalenceClass;
		wordChosen = false;
	}
	
	/**
	 * @return True if the final word is chosen, otherwise false.
	 */
	public boolean isWordChosen() {
		return wordChosen;
	}
	
	/**
	 * Check if the given word contains letters previous guessed.
	 * 
	 * @param word
	 * @return True if the word contains previous guessed characters.
	 */
	public boolean containsPrevGuessed(String word) {
		int idx;
		for(int i = 0; i < nPrevGuessedChars; i++) {
			idx = -1;
			do {
				// Don't check the same letter over and over again.
				idx++;
				
				idx = word.indexOf(prevGuessedChars[i], idx);
				if(idx >= 0 && word.charAt(idx) != equivalenceClass[idx]) {
					return true;
				}
			} while(idx >= 0);
		}
		return false;
	}
	
	/**
	 * Set a flag that indicates that the final word has been chosen.
	 * 
	 * @param b
	 */
	public void setWordChosen(boolean b) {
		wordChosen = b;
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		
		onLoad();
	}
	
	/**
	 * @param equivalenceClass
	 *            The equivalenceClass to set in char array form
	 */
	public void setEquivalenceClass(char[] equivalenceClass) {
		this.equivalenceClass = equivalenceClass;
	}
	
	/**
	 * @return the equivalenceClass
	 */
	public char[] getEquivalenceClass() {
		return equivalenceClass;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.tompeerdeman.ahd.HangmanStatus#getWordChars()
	 */
	@Override
	public char[] getWordChars() {
		return (isWordChosen()) ? equivalenceClass : null;
	}
}
