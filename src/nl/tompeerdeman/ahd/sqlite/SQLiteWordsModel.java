/**
 * File: SQLiteWordDatabase.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.sqlite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.tompeerdeman.ahd.WordsModel;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Tom Peerdeman
 * 
 */
public class SQLiteWordsModel implements WordsModel {
	private final static String SELECT_RAND_INRANGE =
		"SELECT word FROM words WHERE word_length BETWEEN ? AND ? ORDER BY RANDOM() LIMIT 1";
	private final static String SELECT_EQUIVALENT =
		"SELECT word FROM words WHERE word LIKE '?' AND word LIKE '*?*'";
	private final static String INSERT_WORD =
		"INSERT INTO words (word, word_length) VALUES (?, ?)";
	
	private final SQLiteDatabase db;
	
	/**
	 * @param db
	 */
	public SQLiteWordsModel(SQLiteDatabase db) {
		this.db = db;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.tompeerdeman.ahd.WordsModel#getEquivalentWords(java.lang.String,
	 * char)
	 */
	@Override
	public List<String> getEquivalentWords(String like, char contains) {
		Cursor cursor =
			db.rawQuery(SELECT_EQUIVALENT,
					new String[] {like, String.valueOf(contains)});
		int count = cursor.getCount();
		
		if(count == 0) {
			cursor.close();
			return Collections.emptyList();
		} else {
			List<String> list = new ArrayList<String>(count);
			
			while(cursor.moveToNext()) {
				list.add(cursor.getString(0));
			}
			
			cursor.close();
			return list;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.tompeerdeman.ahd.WordsModel#getRandWordInLengthRange(int, int)
	 */
	@Override
	public String getRandWordInLengthRange(int minLength, int maxLength) {
		Cursor cursor =
			db.rawQuery(
					SELECT_RAND_INRANGE,
					new String[] {String.valueOf(minLength),
									String.valueOf(maxLength)});
		
		try {
			if(cursor.getCount() != 1) {
				return null;
			} else {
				cursor.moveToFirst();
				return cursor.getString(0);
			}
		} finally {
			cursor.close();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.tompeerdeman.ahd.WordDatabase#insertWord(java.lang.String)
	 */
	@Override
	public void insertWord(String word) {
		db.execSQL(INSERT_WORD,
				new String[] {word, String.valueOf(word.length())});
	}
}
