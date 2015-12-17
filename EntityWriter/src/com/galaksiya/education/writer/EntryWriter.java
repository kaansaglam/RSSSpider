package com.galaksiya.education.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.galaksiya.education.writer.FilePathInfo;
import com.galaksiya.education.servlet.FeedWriterServlet.Entry;

public class EntryWriter {
	/**
	 * get feed XML and news content parse method. parse XML and content show it
	 * into console
	 * 
	 * @author galaksiya
	 *
	 */
	private static Logger log = Logger.getLogger(EntryWriter.class);

	public void writeFeedEntry(Entry obj) throws ParseException {
		String filePath = obj.getFilePath();
		// filePath always null except test class..
		if (filePath == null) {
			filePath = FilePathInfo.FILE_PATH;
		}
		try (Writer output = new BufferedWriter(new FileWriter(filePath, true));) {

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
		String content = null;
		// get news page source
		Document doc = Jsoup.connect(link).get();
		Elements archived;
		// parse content from xml page with query line(method)
		archived = doc.select(method);
		content = archived.text();
		return content;
	}
}
