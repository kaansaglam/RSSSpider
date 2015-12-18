package com.galaksiya.education.rss.metadata;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.junit.Test;

import com.galaksiya.education.rss.feed.FreshEntryFinder;
import com.galaksiya.education.rss.feed.RSSReader;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;

public class FreshEntryFinderTest {

	private static final String REUTERS = "Reuters";

	// create a String from the contents of a file
	public String readFile() throws FileNotFoundException {
		String filePath = getClass().getResource("FreshEntryFinderTestFile.txt").getPath();
		@SuppressWarnings("resource")
		String textFile = new Scanner(new File(filePath), "UTF-8").useDelimiter("\\A").next();
		return textFile;
	}

	@Test
	public void compareNull() throws IllegalArgumentException, FeedException, IOException {
		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		Iterator<?> itEntries = new RSSReader().parseFeed(readFile());
		SyndEntry entry = (SyndEntry) itEntries.next();
		feedTimeMap.put(REUTERS, null);
		SyndEntry freshEntry = new FreshEntryFinder().isFresh(entry, feedTimeMap.get("missingsource"));
		assertEquals(null, freshEntry);
	}

	@Test
	public void compareExpected() throws IllegalArgumentException, FeedException, IOException, ParseException {
		SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
		Date date = parser.parse("Thu Nov 26 07:41:46 EET 2014");
		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		Iterator<?> itEntries = new RSSReader().parseFeed(readFile());
		SyndEntry entry = (SyndEntry) itEntries.next();
		feedTimeMap.put(REUTERS, date);

		SyndEntry freshEntry = new FreshEntryFinder().isFresh(entry, feedTimeMap.get(REUTERS));

		assertEquals(entry.getTitle(), freshEntry.getTitle());

	}

	@Test
	public void compareDifferentDates() throws IllegalArgumentException, FeedException, IOException {
		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		Iterator<?> itEntries = new RSSReader().parseFeed(readFile());
		Iterator<?> itEntriesTest = new RSSReader().readRSSFeed("https://gaiadergi.com/feed/");

		SyndEntry entry = (SyndEntry) itEntries.next();

		SyndEntry entryTest = (SyndEntry) itEntriesTest.next();
		feedTimeMap.put(REUTERS, entryTest.getPublishedDate());
		SyndEntry freshEntry = new FreshEntryFinder().isFresh(entry, feedTimeMap.get(REUTERS));
		assertEquals(null, freshEntry);
	}
}
