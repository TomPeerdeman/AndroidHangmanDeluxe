/**
 * File: HighScoreFragment.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
			"HH:mm:ss:SS", Locale.US);
	
	static {
		TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
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
		
		tView = (TextView) row.findViewById(R.id.table_id);
		tView.setText("Word");
		
		tView = (TextView) row.findViewById(R.id.table_name);
		tView.setText("Guesses");
		
		tView = (TextView) row.findViewById(R.id.table_time);
		tView.setText("Time");
		
		rootTable.addView(row);
		
		for(int i = 0; i < 8; i++) {
			row =
				(TableRow) inflater.inflate(R.layout.tablerow, rootTable, false);
			
			tView = (TextView) row.findViewById(R.id.table_id);
			tView.setText("Word " + i);
			
			tView = (TextView) row.findViewById(R.id.table_name);
			tView.setText("" + i);
			
			tView = (TextView) row.findViewById(R.id.table_time);
			tView.setText(TIME_FORMAT.format(new Date(i)));
			
			rootTable.addView(row);
		}
		
		return root;
	}
}
