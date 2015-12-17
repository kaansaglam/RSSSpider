package com.galaksiya.education.writer;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.galaksiya.education.servlet.FeedWriterServlet;

public class WriterTest {
	private static final Logger log = Logger.getLogger(FeedWriterServlet.class);

	private String filePath = "/home/galaksiya/Desktop/WriterTest-1.txt";

	@Before
	public void before() throws Exception {
		// clean all file
		PrintWriter write = new PrintWriter(filePath);
		write.print("");
		write.close();

	}

	@After
	public void after() throws Exception {

	}

	@Test
	public void writeIntoEmptyFile() throws ParseException, IOException {
		EntryWriter writer = new EntryWriter();
		// data for write into file
		String title = "test";
		String link = "http://www.teknolog.com/feed/";
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		Date date = format.parse("Thu Dec 10 11:17:27 EET 2015");
		String method = "div.article-container p";

		// call writer method
		writer.writeFeedEntry(new WriteMethodParameter(title, link, date, method, filePath));
		int isEmpty = 0;

		// if file is empty return 1;
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		try {
			if (br.readLine() == null) {
				isEmpty = 1;
			}
		} catch (IOException e) {
			log.warn("IO exception", e);
		}
		br.close();
		assertEquals(0, isEmpty);

	}

	@Test
	public void writeIntoOldFile() throws ParseException, FileNotFoundException {

		EntryWriter writer = new EntryWriter();
		// data for write into file
		String title = "news from turkey";
		String link = "http://www.teknolog.com/feed/";
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		Date date = format.parse("Thu Dec 10 11:17:27 EET 2015");
		String method = "div.article-container p";
		// call writer method
		writer.writeFeedEntry(new WriteMethodParameter(title, link, date, method, filePath));
		writer.writeFeedEntry(new WriteMethodParameter(title, link, date, method, filePath));
		int isFound = 0;
		// search in file title content,
		Scanner txtscan = new Scanner(new File(filePath));

		while (txtscan.hasNextLine()) {
			String str = txtscan.nextLine();
			if (str.contains(title)) {
				isFound = 1;
			}
		}
		txtscan.close();
		assertEquals(1, isFound);
	}
}
