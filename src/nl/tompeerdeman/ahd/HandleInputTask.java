/**
 * File: HandleInputTask.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import nl.tompeerdeman.ahd.game.HangmanGame;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * @author Tom Peerdeman
 *
 */
public class HandleInputTask extends AsyncTask<Character, Void, Void> {
	private final MainActivity main;
	private final ProgressDialog dialog;
	
	public HandleInputTask(MainActivity main) {
		this.main = main;
		dialog = new ProgressDialog(main);
	}
	
	@Override
	protected void onPreExecute() {
		dialog.setMessage("Checking...");
		dialog.show();
	}
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Void doInBackground(Character...params) {
		main.getInputLock().lock();
		char guess  = params[0];
		HangmanGame game = main.getGame();
		
		// Process the guess, the settings are passed by reference so no
		// setSettings is required.
		game.getGameplayDelegate().onGuess(game.getSettings(),
				game.getStatus(), main.getWordDatabase(), guess);
		
		main.getInputLock().unlock();
		return null;
	}
	
	@Override
	protected void onPostExecute(Void v) {
		if(dialog.isShowing()) {
			dialog.dismiss();
		}
		
		main.onKeyCallback();
	}
}
