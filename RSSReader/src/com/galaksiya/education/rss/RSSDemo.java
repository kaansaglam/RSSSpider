package com.galaksiya.education.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Iterator;
import com.galaksiya.education.writer.*;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.xml.sax.SAXException;

import com.galaksiya.education.rss.feed.RSSReader;
import com.galaksiya.education.rss.interaction.MenuPrinter;
import com.galaksiya.education.rss.interaction.UserInteraction;
import com.galaksiya.education.rss.metadata.FeedMetaDataMenager;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;
import com.galaksiya.education.servlet.FeedWriterServlet.Entry;

public class RSSDemo {
	private static Logger log = Logger.getLogger(RSSDemo.class);

	public static void main(String[] args) throws JDOMException, ParserConfigurationException, SAXException,
			IllegalArgumentException, FeedException, IOException, ParseException {

		try {
			Entry entryObj = new Entry();
			FeedMetaDataMenager menageMetaData = new FeedMetaDataMenager();
			UserInteraction interaction = UserInteraction.getInstance();
			interaction.getUserPreferences();
			RSSReader reader = new RSSReader();
			EntryWriter writer = new EntryWriter();

			while (interaction.getAddOrRead() == 2) {
				// the method is for add a new news source
				addNewSource(interaction, menageMetaData, reader, writer, entryObj);
			}
			// the method is for show a source's feed
			writeFeed(interaction, writer, menageMetaData, reader, entryObj);

		} catch (MalformedURLException e) {
			log.error("invalid URL type", e);
		}
	}

	public static void writeFeed(UserInteraction interaction, EntryWriter writer, FeedMetaDataMenager menageMetaData,
			RSSReader reader, Entry entryObj)
					throws IOException, IllegalArgumentException, FeedException, ParseException {

		// get from file sources's URL
		String URL = menageMetaData.readSourceURL(new MenuPrinter().showMenu());

		// get from file source's query method
		String method = menageMetaData.getSourceQuery(URL);
		try {
			String filePath = null;
			// if user choose to show RSS feed into console run FeedWriter
			Iterator<?> itEntries = reader.readRSSFeed(URL);
			if (itEntries != null) {

				while (itEntries.hasNext()) {
					SyndEntry entry = (SyndEntry) itEntries.next();
					entryObj.setlink(entry.getLink());
					entryObj.setTitle(entry.getTitle());
					entryObj.setDate(entry.getPublishedDate().toString());
					entryObj.setMethod(method);
					entryObj.setFilePath(filePath);
					writer.writeFeedEntry(entryObj);
					// char nextFeed = interaction.continueCheck();
					if (!(interaction.continueCheck() == 'y')) {
						break;
					}
				}
			}
		} catch (MalformedURLException e) {
			log.error("invalid URL type", e);
		}
	}

	public static void addNewSource(UserInteraction interaction, FeedMetaDataMenager menageMetaData, RSSReader reader,
			EntryWriter writer, Entry entryObj)
					throws IllegalArgumentException, FeedException, IOException, ParseException {
		Iterator<?> itEntries = reader.readRSSFeed(interaction.getLink());
		String filePath = null;
		if (itEntries != null) {
			// if user choosed to add a news source.
			while (itEntries.hasNext()) {
				SyndEntry entry = (SyndEntry) itEntries.next();
				if (entry != null) {

					entryObj.setlink(entry.getLink());
					entryObj.setTitle(entry.getTitle());
					entryObj.setDate(entry.getPublishedDate().toString());
					entryObj.setMethod(interaction.getMethod());
					entryObj.setFilePath(filePath);
					writer.writeFeedEntry(entryObj);

					// char nextFeed = interaction.continueCheck();
					if (!(interaction.continueCheck() == 'y')) {
						break;
					}
				}
			}
		}

		char addControl = interaction.addOrNot();
		// user decided to add soruce in file.
		if (addControl == 'a') {
			String name = interaction.getName();
			String link = interaction.getLink();
			String method = interaction.getMethod();
			// check is source already exist.
			if (menageMetaData.checkSource(name, link) == true) {
				char entryCheck = interaction.addAlreadyExistRecord();
				if (entryCheck == 'y')
					menageMetaData.addSource(name, link, method);
			} else {
				menageMetaData.addSource(name, link, method);
			}

		}
		interaction.getUserPreferences();
	}
}
