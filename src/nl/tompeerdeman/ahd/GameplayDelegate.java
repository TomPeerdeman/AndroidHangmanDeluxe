package nl.tompeerdeman.ahd;

/**
 * @author Tom Peerdeman
 * 
 */
public interface GameplayDelegate {
	public static final char CHARACTER_UNKNOWN = '_';
	
	/**
	 * Process a guess.
	 * 
	 * @param settings
	 *            The settings of the game
	 * @param status
	 *            The current status of the hangman game
	 * @param guess
	 *            The guessed character
	 * @return The new status of the game
	 */
	public HangmanStatus onGuess(HangmanSettings settings,
			HangmanStatus status, char guess);
	
	/**
	 * Initialize the status of the game.
	 * 
	 * @param settings
	 *            The settings of the game
	 * @return The new status of the game
	 */
	public HangmanStatus initialize(HangmanSettings settings);
}
