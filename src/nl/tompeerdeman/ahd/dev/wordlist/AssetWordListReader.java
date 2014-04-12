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
