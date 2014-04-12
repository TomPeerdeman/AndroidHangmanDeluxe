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
 * File: WordListReaderMain.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.wordlist;

import java.net.MalformedURLException;

/**
 * @author Tom Peerdeman
 * 
 */
public class WordListReaderMain {
	/**
	 * @param args
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws MalformedURLException {
		WordListReader reader =
			new WebWordListReader("http://tompeerdeman.nl/words.xml");
		System.out.printf("Read %d words\n", reader.run());
	}
}
