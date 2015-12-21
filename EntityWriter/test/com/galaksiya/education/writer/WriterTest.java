package com.galaksiya.education.writer;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WriterTest {

	private static final Logger log = Logger.getLogger(WriterTest.class);

	private String filePath = getClass().getResource("WriterTestFile.txt").getPath();

	private EntryWriteRequest entryObj = new EntryWriteRequest();

	@Before
	public void before() throws Exception {
		// clean all file
		PrintWriter write = new PrintWriter(filePath);
		write.print("");
		write.close();

		entryObj.setlink("http://www.teknolog.com/feed/");
		entryObj.setTitle("test title");
		entryObj.setDate("Thu Dec 10 11:17:27 EET 2015");
		entryObj.setMethod("div.article-container p");
		entryObj.setFilePath(filePath);

	}

	@After
	public void after() throws Exception {

	}

	@Test
	public void writeIntoEmptyFile() throws Exception {
		EntryWriter writer = new EntryWriter();

		// call writer method
		writer.writeFeedEntry(entryObj);
		int isEmpty = 0;

		// if file is empty return 1;

		try (BufferedReader br = new BufferedReader(new FileReader(filePath));) {
			if (br.readLine() == null) {
				isEmpty = 1;
			}
		} catch (IOException e) {
			log.warn("IO exception", e);
		}

		assertEquals(0, isEmpty);

	}

	@Test
	public void writeIntoOldFile() throws Exception {

		EntryWriter writer = new EntryWriter();

		// call writer method
		writer.writeFeedEntry(entryObj);
		writer.writeFeedEntry(entryObj);
		int isFound = 0;
		// search in file title content,
		Scanner txtscan = new Scanner(new File(filePath));

		while (txtscan.hasNextLine()) {
			String str = txtscan.nextLine();
			if (str.contains("test title")) {

				isFound = 1;
			}
		}
		txtscan.close();
		assertEquals(1, isFound);
	}
}
