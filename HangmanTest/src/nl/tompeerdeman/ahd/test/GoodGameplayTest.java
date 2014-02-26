/**
 * File: GoodGameplayTest.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import nl.tompeerdeman.ahd.game.GameplayDelegate;
import nl.tompeerdeman.ahd.game.HangmanSettings;
import nl.tompeerdeman.ahd.game.HangmanStatus;
import nl.tompeerdeman.ahd.game.WordsModel;
import nl.tompeerdeman.ahd.game.good.GoodGameplay;

/**
 * @author Tom Peerdeman
 * 
 */
public class GoodGameplayTest extends TestCase {
	private GoodGameplay game;
	private WordsModel db = new WordsModel() {
		@Override
		public void insertWord(String word) {
			throw new RuntimeException("Insert not allowed");
		}
		
		@Override
		public String getRandWordInLengthRange(int minLength, int maxLength) {
			return "WORD WITH SPACES";
		}
		
		@Override
		public List<String> getEquivalentWords(String like, char contains) {
			return Collections.emptyList();
		}
		
		@Override
		public int getNumWords() {
			return 0;
		}
	};
	
	@Override
	public void setUp() {
		game = new GoodGameplay();
	}
	
	/**
	 * Test method for
	 * {@link nl.tompeerdeman.ahd.game.good.GoodGameplay#onGuess(nl.tompeerdeman.ahd.game.HangmanSettings, nl.tompeerdeman.ahd.game.HangmanStatus, nl.tompeerdeman.ahd.game.WordsModel, char)}
	 * .
	 */
	public void testOnGuess() {
		HangmanSettings settings =
			new HangmanSettings((byte) 6, 1, 26, false, false);
		HangmanStatus status = game.initialize(settings, db);
		assertNotNull(status);
		assertEquals(status.getRemainingGuesses(), 6);
		game.onGuess(settings, status, db, 'W');
		assertEquals(status.getRemainingGuesses(), 6);
		assertTrue(new String(status.getGuessedChars()).contains("W"));
		
		// Ignore double guesses
		game.onGuess(settings, status, db, 'W');
		assertEquals(status.getRemainingGuesses(), 6);
		
		// Test not correct guess
		game.onGuess(settings, status, db, 'Q');
		assertEquals(status.getRemainingGuesses(), 5);
	}
	
	/**
	 * Test method for
	 * {@link nl.tompeerdeman.ahd.game.good.GoodGameplay#initialize(nl.tompeerdeman.ahd.game.HangmanSettings, nl.tompeerdeman.ahd.game.WordsModel)}
	 * .
	 */
	public void testInitialize() {
		
		try {
			game.initialize(new HangmanSettings((byte) 6, 1, 26, false, true),
					db);
			fail("Initialize evil on good game succeeded");
		} catch(IllegalArgumentException e) {
			
		}
		
		HangmanStatus status =
			game.initialize(new HangmanSettings((byte) 6, 1, 26, false, false),
					db);
		assertNotNull(status);
		assertFalse(status.hasLostGame());
		assertFalse(status.hasWonGame());
		char[] wordChars = status.getWordChars();
		String origWord = "WORD WITH SPACES";
		assertEquals(wordChars.length, origWord.length());
		assertTrue(Arrays.equals(wordChars, origWord.toCharArray()));
		
		assertEquals(status.getRemainingGuesses(), 6);
		Arrays.fill(wordChars, GameplayDelegate.CHARACTER_UNKNOWN);
		assertTrue(Arrays.equals(status.getGuessedChars(), wordChars));
		
		// Test if spaces are revealed
		status =
			game.initialize(new HangmanSettings((byte) 6, 1, 26, true, false),
					db);
		assertEquals(wordChars.length, origWord.length());
		assertEquals(wordChars.length, status.getGuessedChars().length);
		assertFalse(Arrays.equals(status.getGuessedChars(), wordChars));
		wordChars[4] = ' ';
		wordChars[9] = ' ';
		assertTrue(Arrays.equals(status.getGuessedChars(), wordChars));
	}
}
