/**
 * File: FileWordReader.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.dev.wordlist;

import java.io.IOException;
import java.io.InputStream;

import nl.tompeerdeman.ahd.MainActivity;

/**
 * @author Tom Peerdeman
 * 
 */
public class AssetWordListReader extends WordListReader {
	private InputStream in;
	
	/**
	 * @param main
	 * @param filePath
	 *            name of the asset
	 * @throws IOException
	 */
	public AssetWordListReader(MainActivity main, String filePath)
			throws IOException {
		super(main);
		in = main.getAssets().open(filePath);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.tompeerdeman.ahd.WordListReader#getDataStream()
	 */
	@Override
	protected InputStream getDataStream() {
		return in;
	}
}
