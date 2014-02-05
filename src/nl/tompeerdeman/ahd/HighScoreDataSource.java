/**
 * File: HighScoreDataSource.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

/**
 * @author Tom Peerdeman
 *
 */
public interface HighScoreDataSource {
	public HighScore loadHighScore();
	
	public void saveHighScore(HighScore highScore);
}
