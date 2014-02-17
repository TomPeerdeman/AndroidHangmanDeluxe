/**
 * File: SQLFileWordListReader.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.dev.wordlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import nl.tompeerdeman.ahd.MainActivity;
import nl.tompeerdeman.ahd.WordsModel;

/**
 * @author Tom Peerdeman
 * 
 */
public class WordFileWordListReader extends WordListReader {
	private InputStream in;
	
	/**
	 * @param main
	 * @param filePath 
	 * @throws IOException
	 */
	public WordFileWordListReader(MainActivity main, String filePath)
			throws IOException {
		super(main);
		in = main.getAssets().open(filePath);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.tompeerdeman.ahd.dev.wordlist.WordListReader#getDataStream()
	 */
	@Override
	protected InputStream getDataStream() throws IOException {
		return in;
	}
	
	@Override
	protected Integer doInBackground(WordsModel... wordDatabases) {
		try {
			WordsModel database = wordDatabases[0];
			
			int nWords = 0;
			BufferedReader rIn =
				new BufferedReader(new InputStreamReader(new GZIPInputStream(getDataStream())));
			String line;
			while((line = rIn.readLine()) != null) {
				database.insertWord(line);
				nWords++;
			}
			
			return nWords;
		} catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
