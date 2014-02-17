/**
 * File: SQLiteDatabaseOpener.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Tom Peerdeman
 * 
 */
public class SQLiteDatabaseOpener extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "ahd";
	
	// Table creation SQL statements.
	private static final String WORDS_CREATE =
		"CREATE TABLE words (word TEXT PRIMARY KEY, word_length INTEGER)";
	private static final String HIGHSCORE_CREATE =
		"CREATE TABLE highscore (id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "word TEXT NOT NULL, bad_guesses INTEGER, "
				+ "time INTEGER, game_type INTEGER)";
	
	public SQLiteDatabaseOpener(Context context) {	
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		context.deleteDatabase(DATABASE_NAME);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(WORDS_CREATE);
		db.execSQL(HIGHSCORE_CREATE);
	}
	
	/**
	 * Open a new writable database connection.
	 * 
	 * @return The database connection
	 */
	public SQLiteDatabase open() {
		return getWritableDatabase();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop & recreate tables.
		db.execSQL("DROP TABLE IF EXISTS words");
		db.execSQL("DROP TABLE IF EXISTS highscore");
		onCreate(db);
	}
}
