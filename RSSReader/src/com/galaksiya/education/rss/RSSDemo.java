package com.galaksiya.education.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.xml.sax.SAXException;

import com.galaksiya.education.rss.feed.RSSReader;
import com.galaksiya.education.rss.interaction.MenuPrinter;
import com.galaksiya.education.rss.interaction.UserInteraction;
import com.galaksiya.education.rss.metadata.FeedMetaDataManager;
import com.galaksiya.education.writer.EntryWriteRequest;
import com.galaksiya.education.writer.EntryWriter;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;

/**
 * ask the user add or read add a new source or read a rss source already exist
 * 
 * @author galaksiya
 */
public class RSSDemo {
	private static Logger log = Logger.getLogger(RSSDemo.class);

	public static void main(String[] args) throws JDOMException, ParserConfigurationException, SAXException,
			IllegalArgumentException, FeedException, IOException, ParseException {

		try {
			// object contain (title, link,date,method, filepath)
			EntryWriteRequest entryObj = new EntryWriteRequest();
			FeedMetaDataManager menageMetaData = new FeedMetaDataManager();
			UserInteraction interaction = UserInteraction.getInstance();
			RSSReader reader = new RSSReader();
			EntryWriter writer = new EntryWriter();

			// ask to user add a URI or read already exist URI..
			interaction.getUserPreferences();

			while (interaction.getAddOrRead() == 2) {
				// the method is for add a new news source..
				addNewSource(interaction, menageMetaData, reader, writer, entryObj);
			}
			// the method is for read URI feed..
			writeFeed(interaction, writer, menageMetaData, reader, entryObj);

		} catch (MalformedURLException e) {
			log.error("invalid URL type", e);
		}
	}

	/**
	 * 
	 * 
	 * @param interaction
	 * @param writer
	 * @param menageMetaData
	 * @param reader
	 * @param entryObj
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FeedException
	 * @throws ParseException
	 */
	public static void writeFeed(UserInteraction interaction, EntryWriter writer, FeedMetaDataManager menageMetaData,
			RSSReader reader, EntryWriteRequest entryObj)
					throws IOException, IllegalArgumentException, FeedException, ParseException {

		// get from file sources's URL
		String URL = menageMetaData.readSourceURL(new MenuPrinter().showMenu());

		// get from file , source's content query method
		String method = menageMetaData.getSourceQuery(URL);
		try {
			String filePath = null;
			Iterator<?> itEntries = reader.readRSSFeed(URL);
			if (itEntries != null) {
				// set data into a object
				while (itEntries.hasNext()) {
					SyndEntry entry = (SyndEntry) itEntries.next();
					entryObj.setlink(entry.getLink());
					entryObj.setTitle(entry.getTitle());
					entryObj.setDate(entry.getPublishedDate().toString());
					entryObj.setMethod(method);
					entryObj.setFilePath(filePath);
					// writer method to write data into file
					writer.writeFeedEntry(entryObj);
					// ask to user next entry or finish..
					if (!(interaction.continueCheck() == 'y')) {
						break;
					}
				}
			}
		} catch (MalformedURLException e) {
			log.error("invalid URL type", e);
		}
	}

	/**
	 * add a new rss source in a file..
	 * 
	 * @param interaction
	 * @param menageMetaData
	 * @param reader
	 * @param writer
	 * @param entryObj
	 * @throws IllegalArgumentException
	 * @throws FeedException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void addNewSource(UserInteraction interaction, FeedMetaDataManager menageMetaData, RSSReader reader,
			EntryWriter writer, EntryWriteRequest entryObj)
					throws IllegalArgumentException, FeedException, IOException, ParseException {
		Iterator<?> itEntries = reader.readRSSFeed(interaction.getLink());

		if (itEntries != null) {
			// if user choosed to add a news source.
			while (itEntries.hasNext()) {
				SyndEntry entry = (SyndEntry) itEntries.next();
				if (entry != null) {
					// set data into a object
					entryObj.setlink(entry.getLink());
					entryObj.setTitle(entry.getTitle());
					entryObj.setDate(entry.getPublishedDate().toString());
					entryObj.setMethod(interaction.getMethod());
					String filePath = null;
					entryObj.setFilePath(filePath);
					// writer method to write data into file
					writer.writeFeedEntry(entryObj);
					// ask to user add or not..
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
