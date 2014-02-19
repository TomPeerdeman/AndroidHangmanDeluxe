/**
 * File: SQLiteDatabaseOpener.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.sqlite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
		Log.i("ahd-db", "Create db");
		// Dev code for generating the db.sqlite file in assets.
		db.execSQL(WORDS_CREATE);
		db.execSQL(HIGHSCORE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("ahd-db", "Upgrade db " + oldVersion + " to " + newVersion);
		// Dev code for generating the db.sqlite file in assets.
		// Drop & recreate tables.
		db.execSQL("DROP TABLE IF EXISTS words");
		db.execSQL("DROP TABLE IF EXISTS highscore");
		onCreate(db);
	}
	
	
	/**
	 * Create the db if it doesn't exists.
	 * 
	 * @param context
	 * @param force Force recreate the database
	 * @throws IOException
	 */
	// Java is stupid... Var in is always closed, java says nope.
	@SuppressWarnings("resource")
	public void createDb(Context context, boolean force) throws IOException {
		File dbFile = context.getDatabasePath(DATABASE_NAME);
		if(!dbFile.exists() || force) {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = context.getAssets().open("db.sqlite");
				out = new FileOutputStream(dbFile);
				Log.i("ahd-db", "Copying db.sqlite --> " + dbFile.getName());
				byte[] buf = new byte[1024];
				int n;
				
				while((n = in.read(buf)) > 0) {
					out.write(buf, 0, n);
				}
				Log.i("ahd-db", "Database copied from asset");
			} catch(IOException e) {
				throw e;
			} finally {
				if(in != null) {
					in.close();
				}
				
				if(out != null) {
					out.flush();
					out.close();
				}
			}
		} else {
			Log.i("ahd-db", "Database already exists");
		}
	}
	
	/**
	 * Open a new writable database connection.
	 * 
	 * @return The database connection
	 */
	public SQLiteDatabase open() {
		return getWritableDatabase();
	}
}
