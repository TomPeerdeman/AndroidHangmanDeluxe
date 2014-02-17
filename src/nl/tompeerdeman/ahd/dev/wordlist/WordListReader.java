/**
 * File: WordListReader.java
 * Author: Tom Peerdeman
 */
package nl.tompeerdeman.ahd.dev.wordlist;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import nl.tompeerdeman.ahd.MainActivity;
import nl.tompeerdeman.ahd.WordsModel;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Tom Peerdeman
 * 
 */
public abstract class WordListReader extends
		AsyncTask<WordsModel, Void, Integer> {
	private final MainActivity main;
	private final ProgressDialog dialog;
	
	public WordListReader(MainActivity main) {
		this.main = main;
		dialog = new ProgressDialog(main);
	}
	
	protected abstract InputStream getDataStream() throws IOException;
	
	@Override
	protected void onPreExecute() {
		dialog.setMessage("Downloading words...");
		dialog.show();
	}
	
	@Override
	protected Integer doInBackground(WordsModel... wordDatabases) {
		try {
			WordsModel database = wordDatabases[0];
			// XmlPullParser parser = Xml.newPullParser();
			// XmlPullParser parser =
			// XmlPullParserFactory.newInstance().newPullParser();
			InputStream in = getDataStream();
			
			XMLReader xmlReader =
				SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			WordListXmlParser handler = new WordListXmlParser(database);
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(in));
			
			in.close();
			
			return handler.getNumWords();
			
			// parser.setInput(in, null);
			//
			// Log.i("ahd", "Starting word read...");
			//
			// int nWords = 0;
			// int next;
			// boolean entry = false;
			// while((next = parser.next()) != XmlPullParser.END_DOCUMENT) {
			// if(next == XmlPullParser.START_TAG
			// && parser.getName().equals("item")) {
			// entry = true;
			// } else if(next == XmlPullParser.END_TAG
			// && parser.getName().equals("item")) {
			// entry = false;
			// } else if(next == XmlPullParser.TEXT && entry) {
			// String text = parser.getText();
			// text = text.toLowerCase(Locale.US).trim();
			// database.insertWord(text);
			// nWords++;
			// }
			// }
			//
			// in.close();
			//
			// return nWords;
		} catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	@Override
	protected void onPostExecute(Integer nWords) {
		if(dialog.isShowing()) {
			dialog.dismiss();
		}
		
		Log.i("ahd", "Done word reading " + nWords);
		
		if(nWords > 0) {
			Toast.makeText(main, "Downloaded " + nWords + " words",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(main, "Error", Toast.LENGTH_LONG).show();
		}
	}
}
