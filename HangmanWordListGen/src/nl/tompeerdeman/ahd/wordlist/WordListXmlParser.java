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
package nl.tompeerdeman.ahd.wordlist;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Tom Peerdeman
 * 
 */
public class WordListXmlParser extends DefaultHandler {
	private int nWords;
	private PrintWriter out;
	private GZIPOutputStream gout;
	
	private StringBuffer strBuf;
	
	/**
	 * @param fileName
	 * @throws IOException 
	 */
	public WordListXmlParser(String fileName) throws IOException {
		strBuf = new StringBuffer(16);
		gout = new GZIPOutputStream(new FileOutputStream(fileName));
		out = new PrintWriter(gout);
		//out = new PrintWriter(new FileOutputStream(fileName));
	}
	
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// Clear buffer.
		strBuf.delete(0, strBuf.length());
	}
	
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equals("item")) {
//			out.printf(
//					"INSERT INTO words (word, word_length) VALUES ('%s', %d)\n",
//					strBuf.toString(), strBuf.length());
			out.println(strBuf.toString());
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
	
	public void close() throws IOException {
		gout.flush();
		gout.finish();
		gout.flush();
		out.close();
	}
}
