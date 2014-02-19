/**
 * File: WordListReader.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.wordlist;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * @author Tom Peerdeman
 * 
 */
public abstract class WordListReader {
	protected abstract InputStream getDataStream() throws IOException;
	
	protected Integer run() {
		try {
			InputStream in = getDataStream();
			
			XMLReader xmlReader =
				SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			WordListXmlParser handler = new WordListXmlParser("words.dat");
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(in));
			
			handler.close();
			in.close();
			
			return handler.getNumWords();
		} catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}