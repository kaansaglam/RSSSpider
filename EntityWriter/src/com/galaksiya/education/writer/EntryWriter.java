package com.galaksiya.education.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class EntryWriter {
	/**
	 * get feed XML and news content parse method. parse XML and content show it
	 * into console
	 * 
	 * @author galaksiya
	 *
	 */
	private static final Logger log = Logger.getLogger(EntryWriter.class);

	public void writeFeedEntry(EntryWriteRequest obj) throws IOException {
		String filePath = obj.getFilePath();
		// filePath always null except test class..
		if (filePath == null) {
			filePath = FilePathInfo.FILE_PATH;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		try (Writer output = new BufferedWriter(new FileWriter(file, true));) {
			output.append("\nTitle        : " + obj.getTitle());
			output.append("\nLink         : " + obj.getLink());
			output.append("\nPublish Date : " + obj.getDate());
			// call getContent method and parse rss content.
			output.append("\nContent      : " + getContent(obj.getLink(), obj.getMethod()) + "\n\n\n");

		} catch (IllegalArgumentException e) {
			log.error("warning : can not read feed content !!");
		} catch (IOException e) {
			log.error("General I/O exception: ");
		}

	}

	public String getContent(String link, String method) throws IOException {
		// get news page source
		Document doc = Jsoup.connect(link).get();
		// parse content from xml page with query line(method)
		return doc.select(method).text();
	}
}
