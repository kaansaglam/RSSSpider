
package com.galaksiya.education.rss.feed;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.ParsingFeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * this class read xml file from rss server.
 * 
 * @author galaksiya
 *
 */

public class RSSReader {
	private static final Logger log = Logger.getLogger(RSSReader.class);

	public Iterator<?> readRSSFeed(String sourceUrl)
			throws IllegalArgumentException, MalformedURLException, IOException, ParsingFeedException {
		Iterator<?> itEntries = null;
		try {
			if (sourceUrl != null) {
				// connect the url adress and read data.
				String doc = readURI(sourceUrl);

				if (doc != null) {

					itEntries = parseFeed(doc);

				}
			}
		} catch (MalformedURLException e) {
			log.error("not URL format :" + sourceUrl, e);
		} catch (FeedException e) {
			log.error("feed is not valid  : ", e);
			e.printStackTrace();
		}
		return itEntries;
	}

	public String readURI(String sourceUrl) throws IOException, ParsingFeedException {
		BufferedReader reader = null;
		StringBuilder stringBuilder = null;
		String doc = null;
		try {

			if (sourceUrl != null) {
				// create the HttpURLConnection
				URL url = new URL(sourceUrl);

				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				// want to do an HTTP GET here
				connection.setRequestMethod("GET");

				// give it 15 seconds to respond
				connection.setReadTimeout(15 * 1000);
				connection.connect();

				// read the output from the server
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				stringBuilder = new StringBuilder();

				String line = null;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line + "\n");
				}
				doc = stringBuilder.toString();
			}
		} catch (MalformedURLException e) {
			log.error("could not read " + sourceUrl, e);
		} catch (IOException e) {
			log.error("io exection ", e);
		}
		return doc;
	}

	public Iterator<?> parseFeed(String doc)
			throws FeedException, IOException, UnsupportedEncodingException, ParsingFeedException {
		List<?> entries = null;
		try {
			InputStream inputStream = new ByteArrayInputStream(doc.getBytes("UTF-8"));
			XmlReader reader = new XmlReader(inputStream);
			SyndFeed feed = new SyndFeedInput().build(reader);
			entries = feed.getEntries();
		} catch (FeedException e) {
			log.error("feed is not valid ", e);
		}
		Iterator<?> iterator = null;
		if (entries != null) {
			iterator = entries.iterator();
		}
		return iterator;
	}
}