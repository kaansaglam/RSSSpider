package com.galaksiya.education.rss.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import com.galaksiya.education.rss.feed.FreshEntryFinder;
import com.galaksiya.education.rss.feed.RSSReader;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;
import java.util.Scanner;

public class FreshEntryFinderTest {

	private static final String REUTERS = "Reuters";

	// create a String from the contents of a file
	public String readFile() throws FileNotFoundException {

		@SuppressWarnings("resource")
		String textFile = new Scanner(new File("/home/galaksiya/Desktop/XMLTest.txt"), "UTF-8").useDelimiter("\\A")
				.next();
		return textFile;
	}

	@Test
	public void compareNull() throws IllegalArgumentException, FeedException, IOException {
		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		Iterator<?> itEntries = new RSSReader().parseFeed(readFile());
		SyndEntry entry = (SyndEntry) itEntries.next();
		feedTimeMap.put(REUTERS, null);
		SyndEntry freshEntry = new FreshEntryFinder().compareDates(entry, feedTimeMap, "Retuers");
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

		SyndEntry freshEntry = new FreshEntryFinder().compareDates(entry, feedTimeMap, REUTERS);

		assertEquals(entry.getTitle(), freshEntry.getTitle());

	}

	@Test
	public void compareDifferentDates() throws IllegalArgumentException, FeedException, IOException {
		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		Iterator<?> itEntries = new RSSReader().parseFeed(readFile());
		Iterator<?> itEntriesTest = new RSSReader().readRSSFeed("http://www.teknolog.com/feed/");

		SyndEntry entry = (SyndEntry) itEntries.next();
		feedTimeMap.put(REUTERS, entry.getPublishedDate());
		SyndEntry entryTest = (SyndEntry) itEntriesTest.next();
		SyndEntry freshEntry = new FreshEntryFinder().compareDates(entryTest, feedTimeMap, "Retuers");
		assertEquals(null, freshEntry);
	}
}
