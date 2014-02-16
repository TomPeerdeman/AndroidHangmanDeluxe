/**
 * File: SettingsActivity.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * @author Tom Peerdeman
 * 
 */
public class SettingsActivity extends Activity implements
		OnSeekBarChangeListener {
	private TextView maxGuessesView;
	private TextView minWordLengthView;
	private TextView maxWordLengthView;
	
	private SeekBar maxGuessesBar;
	private SeekBar minWordLengthBar;
	private SeekBar maxWordLengthBar;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		maxGuessesView = (TextView) findViewById(R.id.maxGuessesTextView);
		minWordLengthView = (TextView) findViewById(R.id.minWordLengthTextView);
		maxWordLengthView = (TextView) findViewById(R.id.maxWordLengthTextView);
		
		maxGuessesBar = (SeekBar) findViewById(R.id.maxGuessesBar);
		minWordLengthBar = (SeekBar) findViewById(R.id.minWordLengthBar);
		maxWordLengthBar = (SeekBar) findViewById(R.id.maxWordLengthBar);
		
		maxGuessesBar.setOnSeekBarChangeListener(this);
		minWordLengthBar.setOnSeekBarChangeListener(this);
		maxWordLengthBar.setOnSeekBarChangeListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_settings, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection.
		switch(item.getItemId()) {
			case R.id.action_return_game:
				finish();
				return true;
			case R.id.action_highscore:
				startActivity(new Intent(this, HighScoreActivity.class));
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android
	 * .widget.SeekBar, int, boolean)
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(fromUser) {
			switch(seekBar.getId()) {
				case R.id.maxGuessesBar:
					maxGuessesView.setText("Maximum guesses: " + (progress + 1));
					break;
				case R.id.maxWordLengthBar:
					if(progress < minWordLengthBar.getProgress()) {
						minWordLengthBar.setProgress(progress);
						minWordLengthView.setText("Minimum word length: "
								+ (progress + 1));
					}
					
					maxWordLengthView.setText("Maximum word length: "
							+ (progress + 1));
					break;
				case R.id.minWordLengthBar:
					if(progress > maxWordLengthBar.getProgress()) {
						maxWordLengthBar.setProgress(progress);
						maxWordLengthView.setText("Maximum word length: "
								+ (progress + 1));
					}
					
					minWordLengthView.setText("Minimum word length: "
							+ (progress + 1));
					break;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android
	 * .widget.SeekBar)
	 */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android
	 * .widget.SeekBar)
	 */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
}
