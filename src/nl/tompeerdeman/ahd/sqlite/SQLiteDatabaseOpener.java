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
		// Dev code for generating the db.sqlite file in assets.
		db.execSQL(WORDS_CREATE);
		db.execSQL(HIGHSCORE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Dev code for generating the db.sqlite file in assets.
		// Drop & recreate tables.
		db.execSQL("DROP TABLE IF EXISTS words");
		db.execSQL("DROP TABLE IF EXISTS highscore");
		onCreate(db);
	}
	
	/**
	 * Create the db if the database file does not exists yet, or the creation
	 * is forced.
	 * If the database file does not exists it and its parent directory
	 * structure is created.
	 * The file is then copied from the assets folder.
	 * 
	 * @param context
	 * @param force
	 *            Force recreate the database
	 * @throws IOException
	 */
	// Java is stupid... Var 'in' is always closed, java/eclipse says not.
	@SuppressWarnings("resource")
	public void createDb(Context context, boolean force) throws IOException {
		File dbFile = context.getDatabasePath(DATABASE_NAME);
		if(!dbFile.exists() || force) {
			if(!dbFile.exists()) {
				/*
				 * Android thinks the database file is a directory since the
				 * file has no extension. To create the required directories we
				 * have to grab the parent directory and make sure it exists.
				 */
				dbFile.getParentFile().mkdirs();
				dbFile.createNewFile();
			}
			
			InputStream in = null;
			OutputStream out = null;
			try {
				in = context.getAssets().open("db.sqlite");
				out = new FileOutputStream(dbFile);
				byte[] buf = new byte[1024];
				int n;
				
				while((n = in.read(buf)) > 0) {
					out.write(buf, 0, n);
				}
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
