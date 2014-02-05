/**
 * File: AbstractGameplayDelegate.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import java.util.Random;

/**
 * @author Tom Peerdeman
 * 
 */
public abstract class AbstractGameplayDelegate implements GameplayDelegate {
	protected final WordDatabase wordDatabase;
	protected final Random rand;
	
	/**
	 * @param wordDatabase
	 * @param rand
	 */
	public AbstractGameplayDelegate(WordDatabase wordDatabase, Random rand) {
		this.wordDatabase = wordDatabase;
		this.rand = rand;
	}
	
	/**
	 * Check to see if the character is in the a-z range.
	 * 
	 * @param character
	 * @return
	 */
	protected boolean validChar(char character) {
		return (character <= 'z' && character >= 'a');
	}
}
