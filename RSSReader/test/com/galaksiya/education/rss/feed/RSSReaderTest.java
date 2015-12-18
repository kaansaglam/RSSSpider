package com.galaksiya.education.rss.feed;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.galaksiya.education.rss.metadata.FeedMetaDataManager;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSReaderTest {

	@Test
	public void readRSS() throws IllegalArgumentException, FeedException, IOException {

		Iterator<?> itEntries = null;
		String testUrl = new FeedMetaDataManager().readSourceURL(1);
		URL url = new URL(testUrl);
		HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
		SyndFeedInput input = new SyndFeedInput();
		XmlReader reader = new XmlReader(httpcon);
		SyndFeed feed = input.build(reader);
		List<?> entries = feed.getEntries();
		itEntries = entries.iterator();
		SyndEntry entry = (SyndEntry) itEntries.next();
		Iterator<?> itEntriesTest = new RSSReader().readRSSFeed(testUrl);
		SyndEntry entryTest = (SyndEntry) itEntriesTest.next();

		assertEquals(entry, entryTest);
	}

	@Test(expected = NullPointerException.class)
	public void readUnavailableURL() throws Exception {
		new RSSReader().readRSSFeed("abc").toString();
	}

	@Test(expected = NullPointerException.class)
	public void readNullURL() throws Exception {
		new RSSReader().readRSSFeed("").toString();
	}

}
