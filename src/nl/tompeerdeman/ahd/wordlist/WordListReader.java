/**
 * File: WordListReader.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.wordlist;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import nl.tompeerdeman.ahd.WordsModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * @author Tom Peerdeman
 * 
 */
public abstract class WordListReader {
	protected abstract InputStream getDataStream() throws IOException;
	
	/**
	 * @param database
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public void loadWords(WordsModel database) throws XmlPullParserException,
			IOException {
		XmlPullParser parser = Xml.newPullParser();
		InputStream in = getDataStream();
		parser.setInput(in, null);
		
		int next;
		boolean entry = false;
		while((next = parser.next()) != XmlPullParser.END_DOCUMENT) {
			if(next == XmlPullParser.START_TAG && parser.getName().equals("item")) {
				entry = true;
			} else if(next == XmlPullParser.END_TAG && parser.getName().equals("item")) {
				entry = false;
			} else if(next == XmlPullParser.TEXT && entry) {
				String text = parser.getText();
				text = text.toLowerCase(Locale.US).trim();
				database.insertWord(text);
			}
		}
		
		in.close();
	}
}
