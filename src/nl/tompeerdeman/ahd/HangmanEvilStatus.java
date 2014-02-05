/**
 * File: HangmanEvilStatus.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import java.io.IOException;

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
	 * @param equivalenceClassStr
	 *            The equivalenceClass to set in String form
	 */
	public void setEquivalenceClass(String equivalenceClassStr) {
		setEquivalenceClass(equivalenceClassStr.toCharArray());
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
	
	/**
	 * @param character
	 * @return True if the character is in the eq class
	 */
	public boolean eqClassContains(char character) {
		for(int i = 0; i < equivalenceClass.length; i++) {
			if(equivalenceClass[i] == character) {
				return true;
			}
		}
		return false;
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
