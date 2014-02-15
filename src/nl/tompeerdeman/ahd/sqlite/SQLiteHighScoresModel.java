/**
 * File: SQLiteHighScoresModel.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.sqlite;

import java.util.ArrayList;
import java.util.Collections;

import nl.tompeerdeman.ahd.HighScoreEntry;
import nl.tompeerdeman.ahd.HighScoresModel;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Tom Peerdeman
 * 
 */
public class SQLiteHighScoresModel extends HighScoresModel {
	private static final long serialVersionUID = 1L;
	
	private final static int MAX_HIGHSCORE_DISPLAY = 10;
	
	private final static String INSERT_HIGHSCORE_EVIL =
		"INSERT INTO highscore (word, bad_guesses, time, game_type) "
				+ "VALUES ('?', ?, ?, 0)";
	private final static String INSERT_HIGHSCORE_NORMAL =
		"INSERT INTO highscore (word, bad_guesses, time, game_type) "
				+ "VALUES ('?', ?, ?, 1)";
	
	private final static String REMOVE_OVERFLOW_ALL =
		"DELETE FROM highscore WHERE id IN(" + "SELECT id FROM highscore "
				+ "ORDER BY time DESC, id ASC LIMIT ," + MAX_HIGHSCORE_DISPLAY
				+ ")";
	private final static String REMOVE_OVERFLOW_EVIL =
		"DELETE FROM highscore WHERE id IN("
				+ "SELECT id FROM highscore WHERE game_type = 0 "
				+ "ORDER BY time DESC, id ASC LIMIT ," + MAX_HIGHSCORE_DISPLAY
				+ ")";
	private final static String REMOVE_OVERFLOW_NORMAL =
		"DELETE FROM highscore WHERE id IN("
				+ "SELECT id FROM highscore WHERE game_type = 1 "
				+ "ORDER BY time DESC, id ASC LIMIT ," + MAX_HIGHSCORE_DISPLAY
				+ ")";
	
	private final static String SELECT_COUNT =
		"SELECT COUNT(*), game_type FROM highscore GROUP BY game_type";
	
	private final static String SELECT_HIGHSCORE =
		"SELECT word, bad_guesses, time, game_type FROM highscore "
				+ "ORDER BY time DESC, id ASC LIMIT "
				+ (2 * MAX_HIGHSCORE_DISPLAY);
	
	private final SQLiteDatabase db;
	
	/**
	 * @param db
	 */
	public SQLiteHighScoresModel(SQLiteDatabase db) {
		this.db = db;
		
		// Count the amount of highscore entries for normal and evil.
		Cursor cursor = db.rawQuery(SELECT_COUNT, null);
		while(cursor.moveToNext()) {
			if(cursor.getInt(1) == 0) {
				// Pre-allocate the correct amount.
				highScoresEvil =
					new ArrayList<HighScoreEntry>(cursor.getInt(0));
			} else {
				// Pre-allocate the correct amount.
				highScoresNormal =
					new ArrayList<HighScoreEntry>(cursor.getInt(0));
			}
		}
		cursor.close();
		
		// No evil highscore's found.
		if(highScoresEvil == null) {
			highScoresEvil = Collections.emptyList();
		}
		
		// No normal highscore's found.
		if(highScoresNormal == null) {
			highScoresNormal = Collections.emptyList();
		}
		
		cursor = db.rawQuery(SELECT_HIGHSCORE, null);
		highScoresAll =
			new ArrayList<HighScoreEntry>(Math.min(cursor.getCount(), 10));
		
		while(cursor.moveToNext()) {
			HighScoreEntry entry =
				new HighScoreEntry(cursor.getString(0), cursor.getInt(1),
						cursor.getLong(2));
			
			if(highScoresAll.size() < 10) {
				// Add to combined highscore.
				highScoresAll.add(entry);
			}
			
			if(cursor.getInt(3) == 0) {
				// Add to evil highscore.
				highScoresEvil.add(entry);
			} else {
				// Add to normal highscore.
				highScoresNormal.add(entry);
			}
		}
		
		cursor.close();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.tompeerdeman.ahd.HighScoresModel#insertNew(nl.tompeerdeman.ahd.
	 * HighScoreEntry)
	 */
	@Override
	public void insertNew(HighScoreEntry entry, boolean evil) {
		if(evil) {
			db.execSQL(
					INSERT_HIGHSCORE_EVIL,
					new String[] {entry.getWord(),
									String.valueOf(entry.getBadGuesses()),
									String.valueOf(entry.getTime())});
			
			db.execSQL(REMOVE_OVERFLOW_EVIL);
		} else {
			db.execSQL(
					INSERT_HIGHSCORE_NORMAL,
					new String[] {entry.getWord(),
									String.valueOf(entry.getBadGuesses()),
									String.valueOf(entry.getTime())});
			
			db.execSQL(REMOVE_OVERFLOW_NORMAL);
		}
		
		db.execSQL(REMOVE_OVERFLOW_ALL);
	}
}
