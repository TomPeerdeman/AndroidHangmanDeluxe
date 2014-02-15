/**
 * File: FileWordReader.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.wordlist;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

/**
 * @author Tom Peerdeman
 *
 */
public class AssetWordListReader extends WordListReader {
	private InputStream in;
	
	/**
	 * @param cntx
	 * @param filePath name of the asset
	 * @throws IOException
	 */
	public AssetWordListReader(Context cntx, String filePath) throws IOException {
		in = cntx.getAssets().open(filePath);
	}
	
	/* (non-Javadoc)
	 * @see nl.tompeerdeman.ahd.WordListReader#getDataStream()
	 */
	@Override
	protected InputStream getDataStream() {
		return in;
	}
}
