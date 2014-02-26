/**
 * File: HighScoreFragment.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.highsore;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import nl.tompeerdeman.ahd.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Tom Peerdeman
 * 
 */
public class HighScoreFragment extends Fragment {
	private final static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"HH:mm:ss:SSS", Locale.US);
	
	static {
		// We want a difference of time, so no accounting for the timezone.
		TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	private HighScoresModel highScoresModel;
	public int type;
	
	public HighScoreFragment() {
		highScoresModel = HighScoresModel.getInstance();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root =
			(TableLayout) inflater.inflate(R.layout.fragment_highscore,
					container, false);
		
		TableLayout rootTable =
			(TableLayout) root.findViewById(R.id.highscoreTableLayout);
		
		TableRow row;
		TextView tView;
		
		row = (TableRow) inflater.inflate(R.layout.tablerow, rootTable, false);
		
		tView = (TextView) row.findViewById(R.id.table_word);
		tView.setText("Word");
		
		tView = (TextView) row.findViewById(R.id.table_badguesses);
		tView.setText("Bad guesses");
		
		tView = (TextView) row.findViewById(R.id.table_time);
		tView.setText("Time");
		
		rootTable.addView(row);
		
		highScoresModel.ensureLoaded();
		List<HighScoreEntry> entries;
		switch(type) {
			case 0:
				entries = highScoresModel.getHighScoresEvil();
				break;
			case 1:
				entries = highScoresModel.getHighScoresNormal();
				break;
			case 2:
				entries = highScoresModel.getHighScoresAll();
				break;
			default:
				entries = Collections.emptyList();
		}
		
		for(HighScoreEntry entry : entries) {
			row =
				(TableRow) inflater.inflate(R.layout.tablerow, rootTable, false);
			
			tView = (TextView) row.findViewById(R.id.table_word);
			tView.setText(entry.getWord());
			
			tView = (TextView) row.findViewById(R.id.table_badguesses);
			tView.setText(String.valueOf(entry.getBadGuesses()));
			
			tView = (TextView) row.findViewById(R.id.table_time);
			tView.setText(TIME_FORMAT.format(new Date(entry.getTime())));
			
			rootTable.addView(row);
		}
		
		if(entries.size() == 0) {
			row =
				(TableRow) inflater.inflate(R.layout.tablerow, rootTable, false);
			
			tView = (TextView) row.findViewById(R.id.table_word);
			tView.setText("No entries");
			
			rootTable.addView(row);
		}
		
		return root;
	}
}
