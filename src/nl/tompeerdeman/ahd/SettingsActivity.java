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
/**
 * File: SettingsActivity.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import nl.tompeerdeman.ahd.game.HangmanGame;
import nl.tompeerdeman.ahd.game.HangmanSettings;
import nl.tompeerdeman.ahd.highsore.HighScoreActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * @author Tom Peerdeman
 * 
 */
public class SettingsActivity extends Activity implements
		OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener,
		CompoundButton.OnCheckedChangeListener {
	private HangmanGame game;
	
	private TextView maxGuessesView;
	private TextView minWordLengthView;
	private TextView maxWordLengthView;
	
	private SeekBar maxGuessesBar;
	private SeekBar minWordLengthBar;
	private SeekBar maxWordLengthBar;
	
	private ToggleButton hideNonAlphaButton;
	private RadioGroup gameplayRadioGroup;
	
	private boolean loaded;
	
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
		
		hideNonAlphaButton =
			(ToggleButton) findViewById(R.id.hideNonAlphaToggleButton);
		hideNonAlphaButton.setOnCheckedChangeListener(this);
		gameplayRadioGroup = (RadioGroup) findViewById(R.id.gameplayRadioGroup);
		gameplayRadioGroup.setOnCheckedChangeListener(this);
		
		maxGuessesBar.setOnSeekBarChangeListener(this);
		minWordLengthBar.setOnSeekBarChangeListener(this);
		maxWordLengthBar.setOnSeekBarChangeListener(this);
		
		game = HangmanGame.getInstance();
		
		/*
		 * Indicate settings changes should not result in a new settings object
		 * since the statements below cause them to be called as well.
		 */
		loaded = false;
		
		if(game.getSettings().isEvil()) {
			gameplayRadioGroup.check(R.id.evilRadioButton);
		} else {
			gameplayRadioGroup.check(R.id.niceRadioButton);
		}
		
		hideNonAlphaButton.setChecked(!game.getSettings()
											.shouldRevealNonAlpha());
		
		maxGuessesBar.setProgress(game.getSettings().getMaxNumGuesses() - 1);
		minWordLengthBar.setProgress(game.getSettings().getMinWordLength() - 1);
		maxWordLengthBar.setProgress(game.getSettings().getMaxWordLength() - 1);
		
		// Setting changes should be processed again.
		loaded = true;
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
		switch(seekBar.getId()) {
			case R.id.maxGuessesBar:
				maxGuessesView.setText("Maximum guesses: " + (progress + 1));
				break;
			case R.id.maxWordLengthBar:
				if(progress < minWordLengthBar.getProgress() && fromUser) {
					minWordLengthBar.setProgress(progress);
					minWordLengthView.setText("Minimum word length: "
							+ (progress + 1));
				}
				
				maxWordLengthView.setText("Maximum word length: "
						+ (progress + 1));
				break;
			case R.id.minWordLengthBar:
				if(progress > maxWordLengthBar.getProgress() && fromUser) {
					maxWordLengthBar.setProgress(progress);
					maxWordLengthView.setText("Maximum word length: "
							+ (progress + 1));
				}
				
				minWordLengthView.setText("Minimum word length: "
						+ (progress + 1));
				break;
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
		if(loaded) {
			newSettings();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android
	 * .widget.RadioGroup, int)
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(loaded) {
			newSettings();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged
	 * (android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(loaded) {
			newSettings();
		}
	}
	
	/**
	 * Generate new settings, this is called when a setting is changed.
	 * The settings are not reused, a whole new HangmanSettings object is
	 * created every time.
	 */
	private void newSettings() {
		game.setSettings(new HangmanSettings(
				(byte) (maxGuessesBar.getProgress() + 1),
				(maxWordLengthBar.getProgress() + 1),
				(minWordLengthBar.getProgress() + 1),
				!hideNonAlphaButton.isChecked(),
				(gameplayRadioGroup.getCheckedRadioButtonId() == R.id.evilRadioButton)));
	}
}
