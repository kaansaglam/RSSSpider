package com.galaksiya.education.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.galaksiya.education.writer.FilePathInfo;

public class EntryWriter {
	/**
	 * get feed XML and news content parse method. parse XML and content show it
	 * into console
	 * 
	 * @author galaksiya
	 *
	 */
	private Date lastPublishDate;
	private static Logger log = Logger.getLogger(EntryWriter.class);

	public void writeFeedEntry(WriteMethodParameter parameterObject)
			throws ParseException {
		String filePath = parameterObject.filePath;
		if (filePath == null)
			filePath = FilePathInfo.FILE_PATH;
		try (Writer output = new BufferedWriter(new FileWriter(filePath, true));) {

			// System.out.println("Title : " + title);
			output.append("\nTitle        : " + parameterObject.title);

			// System.out.println("Link : " + link);
			output.append("\nLink         : " + parameterObject.link);

			setLastPublishDate(parameterObject.date);
			// System.out.println("Publish Date : " + getLastPublishDate());
			output.append("\nPublish Date : " + getLastPublishDate());

			// System.out.println("Content : " + archived.text() + "\n\n\n");
			output.append("\nContent      : " + getContent(parameterObject.link, parameterObject.method) + "\n\n\n");

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

	public Date getLastPublishDate() {
		return lastPublishDate;
	}

	public void setLastPublishDate(Date lastPublishDate) {
		this.lastPublishDate = lastPublishDate;
	}

}
