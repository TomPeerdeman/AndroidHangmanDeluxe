/**
 * File: AbstractGameplayDelegate.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;


/**
 * @author Tom Peerdeman
 * 
 */
public abstract class AbstractGameplayDelegate implements GameplayDelegate {
	protected final WordsModel wordDatabase;
	
	/**
	 * @param wordDatabase
	 */
	public AbstractGameplayDelegate(WordsModel wordDatabase) {
		this.wordDatabase = wordDatabase;
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
