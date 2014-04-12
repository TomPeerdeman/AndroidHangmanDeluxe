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
 * File: WebWordListReader.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.wordlist;

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
	public WebWordListReader(String url)
			throws MalformedURLException {
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
