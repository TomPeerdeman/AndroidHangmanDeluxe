/**
 * File: HighScoreActivity.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Tom Peerdeman
 *
 */
public class HighScoreActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_highscore);
	}
}
