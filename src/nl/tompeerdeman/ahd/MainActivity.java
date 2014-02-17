package nl.tompeerdeman.ahd;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import nl.tompeerdeman.ahd.dev.wordlist.WebWordListReader;
import nl.tompeerdeman.ahd.dev.wordlist.WordListReader;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		wordView = (TextView) findViewById(R.id.wordTextView);
		guessesView = (TextView) findViewById(R.id.guessesTextView);
		timeView = (TextView) findViewById(R.id.timeTextView);
		
		wordView.requestFocus();
		
		dbOpener = new SQLiteDatabaseOpener(this);
		db = dbOpener.open();
		
		wordDatabase = new SQLiteWordsModel(db);
		// Empty word list?
		if(wordDatabase.getRandWordInLengthRange(1, 26) == null) {
			try {
				WordListReader reader =
					new WebWordListReader("http://tompeerdeman.nl/words.xml");
				reader.loadWords(wordDatabase);
			} catch(Exception e) {
				// TODO: Inform user instead of crashing.
				e.printStackTrace();
				finish();
				return;
			}
		}
		
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
		
		char guess = (char) event.getUnicodeChar();
		if(HangmanGame.validChar(guess)) {
			// Process the guess, the settings are passed by reference so no
			// setSettings is required.
			game.getGameplayDelegate().onGuess(game.getSettings(),
					game.getStatus(), wordDatabase, guess);
			
			Log.i("ahd-word", new String(game.getStatus().getGuessedChars()));
		}
		
		return super.onKeyUp(keyCode, event);
	}
	
	public void onReset() {
		Log.i("ahd-word", new String(game.getStatus().getGuessedChars()));
		
		wordView.setText("Test");
		final int maxGuesses = game.getSettings().getMaxNumGuesses();
		guessesView.setText("Guesses:\n"
				+ (maxGuesses - game.getStatus().getRemainingGuesses()) + "/"
				+ maxGuesses);
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
	
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(game != null) {
			outState.putSerializable("gameObj", game);
		}
	}
}
