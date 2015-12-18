package com.galaksiya.education.rss.metadata;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.sun.syndication.io.FeedException;

public class FeedMetaDataMenagerTest {
	FeedMetaDataManager menageMetaData = new FeedMetaDataManager();

	@Test
	public void getURL() throws IOException {
		String expectedURL = "http://www.teknolog.com/feed/";
		String returnedURL = menageMetaData.readSourceURL(1);
		assertEquals(expectedURL, returnedURL);
	}

	// for empty row
	@Test
	public void getURLIllegalIndex() throws IOException {
		assertEquals(null, menageMetaData.readSourceURL(0));
	}

	@Test
	public void checkExistent() throws IOException {
		String name = "cnn";
		String link = "www.cnn.com";
		assertTrue(menageMetaData.checkSource(name, link));
	}

	@Test
	public void checkInexistent() throws IOException {
		String name = "asla";
		String link = "asla";
		assertFalse(menageMetaData.checkSource(name, link));
	}

	@Test
	public void getQuery() throws IOException {

		// setup test data...
		String URL = "https://gaiadergi.com/feed/";

		// execute ...
		String returnedURL = menageMetaData.getSourceQuery(URL);

		// assertOutput
		assertEquals("div.entry p", returnedURL);
	}

	@Test
	public void getQueryInexistentURL() throws IOException {
		String nullUrl = "www.nullurl.com";
		String nullQuery = menageMetaData.getSourceQuery(nullUrl);
		assertEquals(null, nullQuery);
	}

	@Test
	public void getQueryNull() throws IOException {
		String url = null;
		assertEquals(null, menageMetaData.getSourceQuery(url));
	}

	@Test
	public void add() throws IllegalArgumentException, IOException, FeedException {
		String name = "haber";
		String link = "www.haber.com";
		String method = "div.content p";

		String[] expectedData = { name, link, method };

		new FeedMetaDataManager().addSource(name, link, method);

		assertArrayEquals(expectedData, getFileData(link));
	}

	@Test
	public void addAlreadyExist() throws IllegalArgumentException, IOException, FeedException {
		String name = "Teknolog";
		String link = "http://www.teknolog.com/feed/";
		String method = "div.article-container p";

		String[] expectedData = { name, link, method };

		menageMetaData.addSource(name, link, method);

		assertArrayEquals(expectedData, getFileData(link));

	}

	public String[] getFileData(String sourceUrl) throws IOException {
		String line = "";
		String cvsSplitBy = ",";
		BufferedReader br = new BufferedReader(new FileReader("/home/galaksiya/Desktop/RSSfile.csv"));
		String method = null;
		String name = null;
		while ((line = br.readLine()) != null) {
			// use comma as separator
			String[] CSVdata = line.split(cvsSplitBy);
			if (sourceUrl.equals(CSVdata[1])) {
				method = CSVdata[2];
				name = CSVdata[0];
			}

		}
		br.close();
		return new String[] { name, sourceUrl, method };
	}
}