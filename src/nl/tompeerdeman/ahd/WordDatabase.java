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
public interface WordDatabase {
	/**
	 * Fetch a list of words like the given one. The character
	 * GameplayDelegate.UNKNOWN_CHARACTER is used for unknown characters.
	 * 
	 * @param like
	 * @return The list of words
	 */
	public List<String> getWordsLike(String like);
	
	/**
	 * @param maxLength
	 * @param minLength
	 * @return The list of words
	 */
	public List<String> getWordsInLength(int maxLength, int minLength);
	
	/**
	 * Insert a word into the database
	 * 
	 * @param word
	 */
	public void insertWord(String word);
	
	/**
	 * @return The amount of words in the database
	 */
	public int getNumWords();
}
