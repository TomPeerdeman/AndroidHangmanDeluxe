/**
 * File: GoodGameplayTest.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import nl.tompeerdeman.ahd.GameplayDelegate;
import nl.tompeerdeman.ahd.GoodGameplay;
import nl.tompeerdeman.ahd.HangmanSettings;
import nl.tompeerdeman.ahd.HangmanStatus;
import nl.tompeerdeman.ahd.WordDatabase;

/**
 * @author Tom Peerdeman
 * 
 */
public class GoodGameplayTest extends TestCase {
	private GoodGameplay game;
	
	@Override
	public void setUp() {
		WordDatabase db = new WordDatabase() {
			@Override
			public List<String> getWordsLike(String like) {
				return new ArrayList<String>();
			}
			
			@Override
			public List<String> getWordsInLength(int maxLength, int minLength) {
				return new ArrayList<String>(Arrays.asList("Word with spaces"));
			}
			
		};
		
		game = new GoodGameplay(db, new Random());
	}
	
	/**
	 * Test method for
	 * {@link nl.tompeerdeman.ahd.GoodGameplay#onGuess(nl.tompeerdeman.ahd.HangmanSettings, nl.tompeerdeman.ahd.HangmanStatus, char)}
	 * .
	 */
	public void testOnGuess() {
		
	}
	
	/**
	 * Test method for
	 * {@link nl.tompeerdeman.ahd.GoodGameplay#initialize(nl.tompeerdeman.ahd.HangmanSettings)}
	 * .
	 */
	public void testInitialize() {
		
		try {
			game.initialize(new HangmanSettings((byte) 6, 1, 26, false, true));
			fail("Initialize evil on good game succeeded");
		} catch(IllegalArgumentException e) {
			
		}
		
		HangmanStatus status =
			game.initialize(new HangmanSettings((byte) 6, 1, 26, false, false));
		assertNotNull(status);
		assertFalse(status.hasLostGame());
		assertFalse(status.hasWonGame());
		char[] wordChars = status.getWordChars();
		String origWord = "Word with spaces";
		assertEquals(wordChars.length, origWord.length());
		assertTrue(Arrays.equals(wordChars, origWord.toCharArray()));
		
		assertEquals(status.getRemainingGuesses(), 6);
		Arrays.fill(wordChars, GameplayDelegate.CHARACTER_UNKNOWN);
		assertTrue(Arrays.equals(status.getGuessedChars(), wordChars));
		
		status =
				game.initialize(new HangmanSettings((byte) 6, 1, 26, true, false));
		assertEquals(wordChars.length, origWord.length());
		assertFalse(Arrays.equals(status.getGuessedChars(), wordChars));
	}
	
}
