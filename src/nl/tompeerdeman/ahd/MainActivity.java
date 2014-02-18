package nl.tompeerdeman.ahd;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.locks.ReentrantLock;

import nl.tompeerdeman.ahd.sqlite.SQLiteDatabaseOpener;
import nl.tompeerdeman.ahd.sqlite.SQLiteWordsModel;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class MainActivity extends Activity {
	private final static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"HH:mm:ss:SS", Locale.US);
	
	static {
		// We want a difference of time, so no accounting for the timezone.
		TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	private SQLiteDatabaseOpener dbOpener;
	private SQLiteDatabase db;
	private WordsModel wordDatabase;
	
	private HangmanGame game;
	
	private TextView wordView;
	private TextView guessesView;
	private TextView timeView;
	
	private ReentrantLock inputLock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		inputLock = new ReentrantLock();
		
		wordView = (TextView) findViewById(R.id.wordTextView);
		guessesView = (TextView) findViewById(R.id.guessesTextView);
		timeView = (TextView) findViewById(R.id.timeTextView);
		
		wordView.requestFocus();
		
		dbOpener = new SQLiteDatabaseOpener(this);
		try {
			dbOpener.createDb(this);
		} catch(IOException e) {
			e.printStackTrace();
			finish();
			return;
		}
		db = dbOpener.open();
		
		wordDatabase = new SQLiteWordsModel(db);
		
		Log.i("ahd-db", "Num words in db: " + wordDatabase.getNumWords());
		Log.i("ahd-db",
				"Get rand word: "
						+ wordDatabase.getRandWordInLengthRange(1, 26));
		
		// Empty word list?
		// if(wordDatabase.getRandWordInLengthRange(1, 26) == null) {
		// try {
		// WordListReader reader =
		// // new WordFileWordListReader(this, "words.dat");
		// new WebWordListReader(this,
		// "http://tompeerdeman.nl/words.xml");
		// Log.i("ahd", "Starting word read");
		// if(reader.execute(wordDatabase) == null) {
		// throw new Exception("Word download failed");
		// }
		// } catch(Exception e) {
		// // TODO: Inform user instead of crashing.
		// e.printStackTrace();
		// finish();
		// return;
		// }
		// }
		
		if(savedInstanceState != null
				&& savedInstanceState.containsKey("gameObj")) {
			game = (HangmanGame) savedInstanceState.getSerializable("gameObj");
			game.onLoad();
			Log.i("ahd", "Load game from bundle");
		} else {
			game = new HangmanGame();
			game.initialize(wordDatabase);
			Log.i("ahd", "Load fresh game");
		}
		
		onReset();
	}
	
	/**
	 * @return the inputLock
	 */
	public ReentrantLock getInputLock() {
		return inputLock;
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
		Log.i("ahd", "Key up " + keyCode);
		
		char guess = (char) Character.toUpperCase(event.getUnicodeChar());
		if(HangmanGame.validChar(guess) && !inputLock.isLocked()) {
			new HandleInputTask(this).execute(guess);
		}
		
		return super.onKeyUp(keyCode, event);
	}
	
	/**
	 * @return the wordDatabase
	 */
	public WordsModel getWordDatabase() {
		return wordDatabase;
	}
	
	public void onKeyCallback() {
		Log.i("ahd-word", new String(game.getStatus().getGuessedChars()));
		showCurrentGuesses();
		showCurrentWord();
	}
	
	public void onReset() {
		Log.i("ahd-word", new String(game.getStatus().getGuessedChars()));
		Log.i("ahd-word",
				new String(
						((HangmanEvilStatus) game.getStatus()).getEquivalenceClass()));
		
		showCurrentWord();
		showCurrentGuesses();
		showCurrentTime();
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
	
	@Override
	public void onPause() {
		super.onPause();
		
		closeKeyboard();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		closeKeyboard();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		closeKeyboard();
		
		if(db != null) {
			db.close();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		/*
		 * Force show the onscreen keyboard.
		 * Source: http://stackoverflow.com/a/6977565
		 */
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
				InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	/**
	 * @return the game
	 */
	public HangmanGame getGame() {
		return game;
	}
	
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i("ahd", "Save called!");
		if(game != null) {
			outState.putSerializable("gameObj", game);
		}
	}
}
