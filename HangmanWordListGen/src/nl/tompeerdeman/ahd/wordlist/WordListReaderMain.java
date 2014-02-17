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
