/**
 * File: EvilGameplay.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Tom Peerdeman
 * 
 */
public class EvilGameplay implements GameplayDelegate {
	private Random rand;
	
	/**
	 */
	public EvilGameplay() {
		rand = new Random();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.tompeerdeman.ahd.GameplayDelegate#onGuess(nl.tompeerdeman.ahd
	 * .HangmanSettings, nl.tompeerdeman.ahd.HangmanStatus, char)
	 */
	@Override
	public HangmanStatus onGuess(HangmanSettings settings,
			HangmanStatus status, WordsModel wordDatabase, final char guess) {
		if(!HangmanGame.validChar(guess)) {
			throw new IllegalArgumentException("Guessed char not in a-z range");
		}
		
		HangmanEvilStatus evilStatus = (HangmanEvilStatus) status;
		
		if(evilStatus.isWordChosen()) {
			// The final word is picked, now we just guess the letters.
			
			char[] word = evilStatus.getWordChars();
			char[] revealed = evilStatus.getGuessedChars();
			
			boolean charFound = false;
			for(int i = 0; i < word.length; i++) {
				// Already guessed and it was already revealed.
				if(revealed[i] == guess) {
					break;
				}
				
				// The guessed character is in the word.
				if(word[i] == guess) {
					charFound = true;
					evilStatus.reveal(i, guess);
				}
			}
			
			// Did not found the guessed character or already guessed this one.
			if(!charFound) {
				evilStatus.decrementGuesses();
			}
		} else {
			if(evilStatus.eqClassContains(guess)) {
				// Character already guessed, ignore.
				evilStatus.decrementGuesses();
			} else {
				List<String> words =
					wordDatabase.getEquivalentWords(
							new String(evilStatus.getEquivalenceClass()), guess);
				if(words.size() == 1) {
					// Only one word left, so we pick it.
					char[] word = words.get(0).toCharArray();
					evilStatus.setEquivalenceClass(word);
					evilStatus.setWordChosen(true);
					
					for(int i = 0; i < word.length; i++) {
						// The guessed character is in the word.
						if(word[i] == guess) {
							evilStatus.reveal(i, guess);
						}
					}
				} else if(words.size() > 1) {
					Map<String, EquivalenceClass> eqClasses =
						new HashMap<String, EquivalenceClass>();
					String eq;
					for(String word : words) {
						// Replace all non guess characters with
						// GameplayDelegate.UNKNOWN_CHARACTER.
						eq = equivalize(word, guess);
						if(eqClasses.containsKey(eq)) {
							// Increment the number of words for this class
							eqClasses.get(eq).addWord();
						} else {
							eqClasses.put(eq, new EquivalenceClass(eq));
						}
					}
					
					EquivalenceClass lowClass = null;
					if(eqClasses.size() > 1) {
						// Get the class with the lowest score.
						int lowScore = Integer.MAX_VALUE;
						for(EquivalenceClass q : eqClasses.values()) {
							int score = q.getScore(guess);
							if(score < lowScore) {
								lowScore = score;
								lowClass = q;
							}
						}
					} else if(eqClasses.size() == 1) {
						// Get the only class.
						lowClass = eqClasses.values().iterator().next();
					}
					
					if(lowClass != null) {
						// Insert the new guess into the old eq class at the
						// positions of the positions in lowClass.
						char[] eqArr = evilStatus.getEquivalenceClass();
						for(int i = 0; i < eqArr.length; i++) {
							if(lowClass.getEqClass().charAt(i) == guess) {
								eqArr[i] = guess;
								evilStatus.reveal(i, guess);
							}
						}
						
						evilStatus.setEquivalenceClass(eqArr);
						
						if(lowClass.getNumWords() == 1) {
							// Only one word left, so we pick it.
							evilStatus.setWordChosen(true);
							
							for(int i = 0; i < eqArr.length; i++) {
								// The guessed character is in the word.
								if(eqArr[i] == guess) {
									evilStatus.reveal(i, guess);
								}
							}
						}
					}
				} else {
					// No words found combined with the current eq class.
					evilStatus.decrementGuesses();
				}
			}
		}
		
		return evilStatus;
	}
	
	private String equivalize(String word, char keep) {
		return word.replaceAll("[^" + keep + "]",
				String.valueOf(GameplayDelegate.CHARACTER_UNKNOWN));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.tompeerdeman.ahd.GameplayDelegate#initialize(nl.tompeerdeman.ahd
	 * .HangmanSettings)
	 */
	@Override
	public HangmanStatus initialize(HangmanSettings settings,
			WordsModel wordDatabase) {
		if(!settings.isEvil()) {
			throw new IllegalArgumentException(
					"Initialize evil gameplay but settings say nice gameplay");
		}
		
		// Grab a random word to determine the size.
		String word =
			wordDatabase.getRandWordInLengthRange(settings.getMinWordLength(),
					settings.getMaxWordLength());
		
		// Build equivalence class
		char[] eqClass = new char[word.length()];
		Arrays.fill(eqClass, GameplayDelegate.CHARACTER_UNKNOWN);
		
		return new HangmanEvilStatus(settings.getMaxNumGuesses(), eqClass);
	}
	
	private class EquivalenceClass {
		private final String eqClass;
		private int nWords;
		
		/**
		 * @param eqClass
		 */
		public EquivalenceClass(String eqClass) {
			this.eqClass = eqClass;
			nWords = 1;
		}
		
		/**
		 * @return the eqClass
		 */
		public String getEqClass() {
			return eqClass;
		}
		
		public void addWord() {
			nWords++;
		}
		
		public int getNumWords() {
			return nWords;
		}
		
		public int getScore(char guess) {
			return (countOccurences(guess) * 100) / nWords + rand.nextInt(10);
		}
		
		private int countOccurences(char needle) {
			return eqClass.length()
					- eqClass.replaceAll(String.valueOf(needle), "").length();
		}
	}
}
