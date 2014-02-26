/**
 * File: HangmanGame.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.game;

import java.io.Serializable;

import nl.tompeerdeman.ahd.game.evil.EvilGameplay;
import nl.tompeerdeman.ahd.game.good.GoodGameplay;

/**
 * @author Tom Peerdeman
 * 
 */
public class HangmanGame implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static HangmanGame instance;
	
	private HangmanStatus status;
	private HangmanSettings settings;
	private HangmanSettings newSettings;
	
	private transient GameplayDelegate gameplayDelegate;
	
	/**
	 * @return The single instance of the game
	 */
	public static HangmanGame getInstance() {
		return instance;
	}
	
	/**
	 * Check to see if the character is in the a-z range.
	 * 
	 * @param character
	 * @return True if the character is in the a-z range
	 */
	public static boolean validChar(char character) {
		return(character <= 'Z' && character >= 'A');
	}
	
	public HangmanGame() {
		HangmanGame.instance = this;
	}
	
	/**
	 * Initialize a new game.
	 * 
	 * @param wordDatabase
	 */
	public void initialize(WordsModel wordDatabase) {
		if(newSettings != null) {
			settings = newSettings;
		}
		
		if(settings == null) {
			settings = HangmanSettings.getDefaultSettings();
		}
		
		// Create the gameplay delegate based on the settings.
		if(gameplayDelegate == null) {
			if(settings.isEvil()) {
				gameplayDelegate = new EvilGameplay();
			} else {
				gameplayDelegate = new GoodGameplay();
			}
		} else if(gameplayDelegate instanceof EvilGameplay
				&& !settings.isEvil()) {
			gameplayDelegate = new GoodGameplay();
		} else if(gameplayDelegate instanceof GoodGameplay && settings.isEvil()) {
			gameplayDelegate = new EvilGameplay();
		}
		
		// Generate new status.
		status = gameplayDelegate.initialize(settings, wordDatabase);
	}
	
	/**
	 * Initialize a game that has been loaded from storage.
	 */
	public void onLoad() {
		HangmanGame.instance = this;
		
		// Create the gameplay delegate based on the settings.
		if(settings.isEvil()) {
			gameplayDelegate = new EvilGameplay();
		} else {
			gameplayDelegate = new GoodGameplay();
		}
	}
	
	/**
	 * @return the status
	 */
	public HangmanStatus getStatus() {
		return status;
	}
	
	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(HangmanStatus status) {
		this.status = status;
	}
	
	/**
	 * @return the settings
	 */
	public HangmanSettings getSettings() {
		if(newSettings == null)
			return settings;
		else
			return newSettings;
	}
	
	/**
	 * @param settings
	 *            the settings to set
	 */
	public void setSettings(HangmanSettings settings) {
		this.newSettings = settings;
	}
	
	/**
	 * @return the gameplayDelegate
	 */
	public GameplayDelegate getGameplayDelegate() {
		return gameplayDelegate;
	}
}
