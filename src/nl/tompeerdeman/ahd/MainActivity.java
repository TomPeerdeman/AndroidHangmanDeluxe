/*******************************************************************************
 * Copyright (c) 2014 Tom Peerdeman.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Tom Peerdeman - initial API and implementation
 ******************************************************************************/
package nl.tompeerdeman.ahd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.locks.ReentrantLock;

import nl.tompeerdeman.ahd.game.HangmanGame;
import nl.tompeerdeman.ahd.game.WordsModel;
import nl.tompeerdeman.ahd.highsore.HighScoreActivity;
import nl.tompeerdeman.ahd.highsore.HighScoreEntry;
import nl.tompeerdeman.ahd.highsore.HighScoresModel;
import nl.tompeerdeman.ahd.sqlite.SQLiteDatabaseOpener;
import nl.tompeerdeman.ahd.sqlite.SQLiteHighScoresModel;
import nl.tompeerdeman.ahd.sqlite.SQLiteWordsModel;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private final static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"HH:mm:ss:SS", Locale.US);
	
	static {
		// We want a difference of time, so no accounting for the timezone.
		TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	private SQLiteDatabaseOpener dbOpener;
	private SQLiteDatabase db;
	private WordsModel wordDatabase;
	private HighScoresModel highScoreModel;
	
	private HangmanGame game;
	
	private TextView wordView;
	private TextView guessesView;
	private TextView timeView;
	
	private ImageView imageView;
	private HangmanDrawable statusDrawable;
	
	private ReentrantLock inputLock;
	
	private HangmanTimer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		inputLock = new ReentrantLock();
		
		wordView = (TextView) findViewById(R.id.wordTextView);
		guessesView = (TextView) findViewById(R.id.guessesTextView);
		timeView = (TextView) findViewById(R.id.timeTextView);
		
		imageView = (ImageView) findViewById(R.id.statusImageView);
		
		Bitmap mainBitmap =
			BitmapFactory.decodeResource(getResources(),
					R.drawable.hangman_stand);
		
		statusDrawable = new HangmanDrawable(mainBitmap);
		imageView.setImageDrawable(statusDrawable);
		
		((Button) findViewById(R.id.openKeyboardButton)).setOnClickListener(this);
		
		wordView.requestFocus();
		
		dbOpener = new SQLiteDatabaseOpener(this);
		try {
			dbOpener.createDb(this, false);
		} catch(IOException e) {
			e.printStackTrace();
			finish();
			return;
		}
		db = dbOpener.open();
		
		wordDatabase = new SQLiteWordsModel(db);
		
		/*
		 * Dev code. Load words from XML into the database.
		 * Since this is too slow the whole database is copied from the assets
		 * intead.
		 * 
		 * // Empty word list?
		 * if(wordDatabase.getRandWordInLengthRange(1, 26) == null) {
		 * try {
		 * WordListReader reader =
		 * // new WordFileWordListReader(this, "words.dat");
		 * new WebWordListReader(this,
		 * "http://tompeerdeman.nl/words.xml");
		 * if(reader.execute(wordDatabase) == null) {
		 * throw new Exception("Word download failed");
		 * }
		 * } catch(Exception e) {
		 * e.printStackTrace();
		 * finish();
		 * return;
		 * }
		 * }
		 */
		
		// No words, recreate the database from scratch.
		if(wordDatabase.getNumWords() == 0) {
			wordDatabase = null;
			db.close();
			
			try {
				dbOpener.createDb(this, true);
			} catch(IOException e) {
				e.printStackTrace();
				finish();
				return;
			}
			
			db = dbOpener.open();
			
			wordDatabase = new SQLiteWordsModel(db);
		}
		
		highScoreModel = new SQLiteHighScoresModel(db);
		
		timer = new HangmanTimer(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		timer.stopTimer();
		closeKeyboard();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		openKeyboard();
		
		if(game == null) {
			readGameStatus();
			
			if(game == null) {
				game = new HangmanGame();
				game.initialize(wordDatabase);
			}
			onReset();
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		timer.stopTimer();
		closeKeyboard();
		
		writeGameStatus();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		timer.stopTimer();
		closeKeyboard();
		
		if(db != null) {
			db.close();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		openKeyboard();
		showPausedDialog();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection.
		switch(item.getItemId()) {
			case R.id.action_new_game:
				newGame(true);
				
				return true;
			case R.id.action_highscore:
				startActivity(new Intent(this, HighScoreActivity.class));
				return true;
			case R.id.action_settings:
				startActivity(new Intent(this, SettingsActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * Handle input events from the keyboard. When the game is in lock state or
	 * the game has been finished this input is ignored. Otherwise this method
	 * fire's an async task that should call the required methods to handle the
	 * guess and handle the response.
	 * 
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		char guess = (char) Character.toUpperCase(event.getUnicodeChar());
		// Only process valid char's and if we are in the right state.
		if(HangmanGame.validChar(guess) && !inputLock.isLocked()
				&& !game.getStatus().hasLostGame()
				&& !game.getStatus().hasWonGame()) {
			timer.stopTimer();
			new HandleInputTask(this).execute(guess);
		}
		
		return super.onKeyUp(keyCode, event);
	}
	
	/**
	 * Callback from the async task that onKeyUp fired.
	 * This method updates the visualization of the status and shows the dialogs
	 * if the game has been won or lost.
	 * 
	 * If the game has been won this method inserts the game into the highscore.
	 */
	public void onKeyCallback() {
		showCurrentGuesses();
		showCurrentWord();
		
		wordView.requestFocus();
		
		// Set new progress level (0%-100%).
		statusDrawable.setLevel(100 - (int) Math.round((double) 100.0
				* ((double) game.getStatus().getRemainingGuesses() / (double) game.getSettings()
																					.getMaxNumGuesses())));
		// Redraw the visualization of the status.
		statusDrawable.invalidateSelf();
		
		if(game.getStatus().hasLostGame()) {
			char[] wordChars = game.getStatus().getWordChars();
			String msg = "You lost!";
			
			// Show the word if it was chosen.
			if(wordChars != null) {
				msg += "\n\nThe word was: \n" + new String(wordChars);
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(msg)
					.setCancelable(false)
					.setPositiveButton("New game",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									newGame(false);
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		} else if(game.getStatus().hasWonGame()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			int[] highPos =
				highScoreModel.getHighScorePosition(game.getStatus().getTime());
			String allPos =
				"Overall: "
						+ ((highPos[2] <= HighScoresModel.MAX_HIGHSCORE_DISPLAY)
							? "position " + highPos[2] : "not listed");
			String specificPos;
			if(game.getSettings().isEvil()) {
				specificPos =
					"Evil: "
							+ ((highPos[0] <= HighScoresModel.MAX_HIGHSCORE_DISPLAY)
								? "position " + highPos[0] : "not listed");
			} else {
				specificPos =
					"Normal: "
							+ ((highPos[1] <= HighScoresModel.MAX_HIGHSCORE_DISPLAY)
								? "position " + highPos[1] : "not listed");
			}
			
			builder.setMessage(
					"You won!\nDid you make the highscore?\n" + allPos + "\n"
							+ specificPos)
					.setCancelable(false)
					.setPositiveButton("To highscores",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									startActivity(new Intent(MainActivity.this,
											HighScoreActivity.class));
								}
							})
					.setNegativeButton("New game",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									newGame(false);
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			
			// Try to insert the new highscore.
			highScoreModel.insertNew(new HighScoreEntry(game),
					game.getSettings().isEvil());
		} else {
			timer.startTimer();
		}
	}
	
	/**
	 * Create a new game.
	 * This method initializes a new status, and the old status is discarded.
	 * 
	 * 
	 * @param pause
	 *            Should the "Game paused" dialog be shown.
	 */
	private void newGame(boolean pause) {
		timer.stopTimer();
		
		// Reset game status & settings.
		game.initialize(wordDatabase);
		
		onReset();
		openKeyboard();
		if(pause)
			showPausedDialog();
		else
			timer.startTimer();
	}
	
	/**
	 * This method resets the visible elements to a state where a fresh game is
	 * visible.
	 */
	public void onReset() {
		showCurrentWord();
		showCurrentGuesses();
		showCurrentTime();
		
		Bitmap secBitmap;
		int offsX;
		int offsY;
		if(game.getSettings().isEvil()) {
			secBitmap =
				BitmapFactory.decodeResource(getResources(), R.drawable.apple);
			offsX = -2;
			offsY = 7;
		} else {
			secBitmap =
				BitmapFactory.decodeResource(getResources(), R.drawable.android);
			offsX = 0;
			offsY = -3;
		}
		
		statusDrawable.setSecBitmap(secBitmap, offsX, offsY);
		
		// Reset status drawing & redraw.
		statusDrawable.setLevel(0);
		statusDrawable.invalidateSelf();
	}
	
	/**
	 * Update the visible text to show the correct and not yet guessed
	 * characters
	 */
	public void showCurrentWord() {
		char[] word = game.getStatus().getGuessedChars();
		char[] newWord = new char[word.length * 2];
		for(int i = 0, j = 0; j < word.length; i += 2, j++) {
			newWord[i] = word[j];
			newWord[i + 1] = ' ';
		}
		wordView.setText(new String(newWord));
	}
	
	/**
	 * Update the visible text to show the amount of guesses left.
	 */
	public void showCurrentGuesses() {
		final int maxGuesses = game.getSettings().getMaxNumGuesses();
		guessesView.setText("Guesses:\n"
				+ (maxGuesses - game.getStatus().getRemainingGuesses()) + "/"
				+ maxGuesses);
	}
	
	/**
	 * Update the visible text to show the time since the game has started.
	 */
	public void showCurrentTime() {
		timeView.setText("Time:\n"
				+ TIME_FORMAT.format(new Date(game.getStatus().getTime())));
	}
	
	private void closeKeyboard() {
		/*
		 * Close the onscreen keyboard.
		 * Source: http://stackoverflow.com/a/6977565
		 */
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				wordView.getWindowToken(), 0);
	}
	
	private void openKeyboard() {
		/*
		 * Force show the onscreen keyboard.
		 * Source: http://stackoverflow.com/a/6977565
		 */
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
				InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
		
		wordView.requestFocus();
	}
	
	/**
	 * Show the paused dialog.
	 * If the game is not finished yet the text "Game paused" is shown
	 * else the text "Game finished" is shown.
	 * 
	 * This method also provides the actions for the dialog buttons.
	 * 
	 */
	private void showPausedDialog() {
		timer.stopTimer();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		if(!game.getStatus().hasLostGame() && !game.getStatus().hasWonGame()) {
			builder.setMessage("Game paused")
					.setCancelable(false)
					.setPositiveButton("Resume",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									timer.startTimer();
								}
							});
			
		} else {
			builder.setMessage("Game finished")
					.setCancelable(false)
					.setPositiveButton("New game",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									newGame(false);
								}
							});
		}
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	/**
	 * Write the current game to a file.
	 */
	private void writeGameStatus() {
		ObjectOutputStream objOut = null;
		try {
			objOut =
				new ObjectOutputStream(openFileOutput("game.ser",
						Context.MODE_PRIVATE));
			objOut.writeObject(game);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(objOut != null) {
				try {
					objOut.flush();
					objOut.close();
				} catch(IOException dontcare) {
				}
			}
		}
	}
	
	/**
	 * Load a game from a file. The loading may fail (if no game is saved), in
	 * that case nothing is done.
	 */
	private void readGameStatus() {
		ObjectInputStream objIn = null;
		try {
			objIn = new ObjectInputStream(openFileInput("game.ser"));
			HangmanGame game_tmp = (HangmanGame) objIn.readObject();
			if(game_tmp != null) {
				game = game_tmp;
				game.onLoad();
			}
		} catch(FileNotFoundException dontcare) {
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException dontcare) {
		} finally {
			if(objIn != null) {
				try {
					objIn.close();
				} catch(IOException dontcare) {
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * Handle the clicks from the "Open keyboard" button 
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		openKeyboard();
	}
	
	/**
	 * @return the inputLock
	 */
	public ReentrantLock getInputLock() {
		return inputLock;
	}
	
	/**
	 * @return the wordDatabase
	 */
	public WordsModel getWordDatabase() {
		return wordDatabase;
	}
	
	/**
	 * @return the game
	 */
	public HangmanGame getGame() {
		return game;
	}
}
