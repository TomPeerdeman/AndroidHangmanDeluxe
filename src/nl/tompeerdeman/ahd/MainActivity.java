package nl.tompeerdeman.ahd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
	    switch (item.getItemId()) {
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
}
