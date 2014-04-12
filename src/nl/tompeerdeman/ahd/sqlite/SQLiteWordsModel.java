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
 * File: SQLiteWordDatabase.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.sqlite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.tompeerdeman.ahd.game.WordsModel;
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
		"SELECT word FROM words WHERE word_length = ? AND word LIKE ? AND word LIKE ?";
	private final static String INSERT_WORD =
		"INSERT INTO words (word, word_length) VALUES (?, ?)";
	private final static String COUNT_WORDS = "SELECT COUNT(*) FROM words";
	
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
					new String[] {String.valueOf(like.length()), like,
									"%" + String.valueOf(contains) + "%"});
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.tompeerdeman.ahd.WordsModel#getNumWords()
	 */
	@Override
	public int getNumWords() {
		Cursor cursor = db.rawQuery(COUNT_WORDS, null);
		
		try {
			if(cursor.getCount() != 1) {
				return -1;
			} else {
				cursor.moveToFirst();
				return cursor.getInt(0);
			}
		} finally {
			cursor.close();
		}
	}
}
