/**
 * File: WordDatabank.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import java.util.List;

/**
 * @author Tom Peerdeman
 * 
 */
public interface WordsModel {
	/**
	 * Fetch a list of words like the given one that contain the given
	 * character. The character GameplayDelegate.UNKNOWN_CHARACTER is used for
	 * unknown characters.
	 * 
	 * @param like
	 * @param contains
	 * @return The list of words (may be empty)
	 */
	public List<String> getEquivalentWords(String like, char contains);
	
	/**
	 * Retrieve a random word where it's length is in the given range.
	 * 
	 * @param minLength
	 * @param maxLength
	 * @return The random word or null if no word exists in this length range
	 */
	public String getRandWordInLengthRange(int minLength, int maxLength);
	
	/**
	 * Insert a word into the database
	 * 
	 * @param word
	 */
	public void insertWord(String word);
}
