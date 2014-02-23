package nl.tompeerdeman.ahd;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.locks.ReentrantLock;

import nl.tompeerdeman.ahd.sqlite.SQLiteDatabaseOpener;
import nl.tompeerdeman.ahd.sqlite.SQLiteHighScoresModel;
import nl.tompeerdeman.ahd.sqlite.SQLiteWordsModel;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
	
	private ReentrantLock inputLock;
	private boolean paused;
	
	private Handler myHandler;
	private long startTime;
	private Runnable updateTime = new Runnable() {
		public void run() {
			if(paused) {
				return;
			}
			
			game.getStatus().setTime(SystemClock.elapsedRealtime() - startTime);
			showCurrentTime();
			
			myHandler.postDelayed(updateTime, 100);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		inputLock = new ReentrantLock();
		myHandler = new Handler();
		paused = false;
		
		wordView = (TextView) findViewById(R.id.wordTextView);
		guessesView = (TextView) findViewById(R.id.guessesTextView);
		timeView = (TextView) findViewById(R.id.timeTextView);
		
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
		 * Empty word list?
		 * if(wordDatabase.getRandWordInLengthRange(1, 26) == null) {
		 * try {
		 * WordListReader reader =
		 * // new WordFileWordListReader(this, "words.dat");
		 * new WebWordListReader(this,
		 * "http://tompeerdeman.nl/words.xml");
		 * Log.i("ahd", "Starting word read");
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
		
		if(savedInstanceState != null
				&& savedInstanceState.containsKey("gameObj")) {
			game = (HangmanGame) savedInstanceState.getSerializable("gameObj");
			game.onLoad();
		} else {
			game = new HangmanGame();
			game.initialize(wordDatabase);
		}
		
		onReset();
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
				newGame();
				
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
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		char guess = (char) Character.toUpperCase(event.getUnicodeChar());
		// Only process valid char's and if we are in the right state.
		if(HangmanGame.validChar(guess) && !inputLock.isLocked()
				&& !game.getStatus().hasLostGame()
				&& !game.getStatus().hasWonGame()) {
			stopTimer();
			new HandleInputTask(this).execute(guess);
		}
		
		return super.onKeyUp(keyCode, event);
	}
	
	public void onKeyCallback() {
		showCurrentGuesses();
		showCurrentWord();
		
		wordView.requestFocus();
		
		if(game.getStatus().hasLostGame()) {
			stopTimer();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("You lost!")
					.setCancelable(false)
					.setPositiveButton("New game",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									newGame();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		} else if(game.getStatus().hasWonGame()) {
			stopTimer();
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
									newGame();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			
			// Try to insert the new highscore.
			highScoreModel.insertNew(new HighScoreEntry(game),
					game.getSettings().isEvil());
		} else {
			onGameResume();
		}
	}
	
	private void newGame() {
		stopTimer();
		
		game.initialize(wordDatabase);
		
		onReset();
	}
	
	public void onReset() {
		startTime = SystemClock.elapsedRealtime();
		myHandler.removeCallbacks(updateTime);
		game.getStatus().setTime(0);
		
		showCurrentWord();
		showCurrentGuesses();
		showCurrentTime();
		
		paused = false;
		startTime = SystemClock.elapsedRealtime();
		myHandler.postDelayed(updateTime, 100);
	}
	
	public void showCurrentWord() {
		char[] word = game.getStatus().getGuessedChars();
		char[] newWord = new char[word.length * 2];
		for(int i = 0, j = 0; j < word.length; i += 2, j++) {
			newWord[i] = word[j];
			newWord[i + 1] = ' ';
		}
		wordView.setText(new String(newWord));
	}
	
	public void showCurrentGuesses() {
		final int maxGuesses = game.getSettings().getMaxNumGuesses();
		guessesView.setText("Guesses:\n"
				+ (maxGuesses - game.getStatus().getRemainingGuesses()) + "/"
				+ maxGuesses);
	}
	
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
	
	private void showKeyboard() {
		/*
		 * Force show the onscreen keyboard.
		 * Source: http://stackoverflow.com/a/6977565
		 */
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
				InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	private void stopTimer() {
		paused = true;
		myHandler.removeCallbacksAndMessages(updateTime);
	}
	
	private void onGameResume() {
		if(!game.getStatus().hasLostGame() && !game.getStatus().hasWonGame()) {
			paused = false;
			showCurrentTime();
		}
		
		showKeyboard();
		
		if(!game.getStatus().hasLostGame() && !game.getStatus().hasWonGame()) {
			startTime =
				SystemClock.elapsedRealtime() - game.getStatus().getTime();
			myHandler.postDelayed(updateTime, 100);
		}
		
		wordView.requestFocus();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		stopTimer();
		closeKeyboard();
		
		updateTime.run();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		stopTimer();
		closeKeyboard();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		stopTimer();
		closeKeyboard();
		
		if(db != null) {
			db.close();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		onGameResume();
	}
	
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(game != null) {
			outState.putSerializable("gameObj", game);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		showKeyboard();
	}
}
