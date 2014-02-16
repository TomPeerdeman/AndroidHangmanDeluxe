/**
 * File: WebWordListReader.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.dev.wordlist;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Tom Peerdeman
 * 
 */
public class WebWordListReader extends WordListReader {
	private final URL url;
	
	/**
	 * @param url
	 * @throws MalformedURLException
	 */
	public WebWordListReader(String url) throws MalformedURLException {
		this.url = new URL(url);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.tompeerdeman.ahd.WordListReader#getDataStream()
	 */
	@Override
	protected InputStream getDataStream() throws IOException {
		return url.openStream();
	}
}
