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
 * File: WordListXmlParser.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.dev.wordlist;

import nl.tompeerdeman.ahd.WordsModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Tom Peerdeman
 * 
 */
public class WordListXmlParser extends DefaultHandler {
	private WordsModel wordDatabase;
	private int nWords;
	
	private StringBuffer strBuf;
	
	/**
	 * @param wordDatabase
	 */
	public WordListXmlParser(WordsModel wordDatabase) {
		this.wordDatabase = wordDatabase;
		strBuf = new StringBuffer(16);
	}
	
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// Clear buffer.
		strBuf.delete(0, strBuf.length());
	}
	
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equals("item")) {
			wordDatabase.insertWord(strBuf.toString());
			nWords++;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		strBuf.append(ch, start, length);
	}
	
	public int getNumWords() {
		return nWords;
	}
}
