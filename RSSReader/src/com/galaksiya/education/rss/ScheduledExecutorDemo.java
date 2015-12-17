package com.galaksiya.education.rss;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.galaksiya.education.rss.connection.ServletConnection;
import com.galaksiya.education.rss.feed.FreshEntryFinder;
import com.galaksiya.education.rss.feed.RSSReader;
import com.galaksiya.education.rss.interaction.MenuPrinter;
import com.galaksiya.education.rss.metadata.FeedMetaDataMenager;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;

public class ScheduledExecutorDemo {
	private static final Logger log = Logger.getLogger(ScheduledExecutorDemo.class);

	public static void main(String[] args) throws ParseException, IllegalArgumentException, IOException, FeedException {
		Map<String, Date> feedTimeMap = new HashMap<String, Date>();
		int sourceCount = new MenuPrinter().getSourceCount();
		ExecutorService executorService = Executors.newFixedThreadPool(sourceCount);
		Runnable runnable = new Runnable() {
			public void run() {
				for (int i = 1; i <= sourceCount; i++) {
					final int count = i;
					executorService.submit(new Runnable() {
						@Override
						public void run() {
							runEntry(sourceCount, feedTimeMap, count);
						}
					});
				}
			}
		};
		// run read cycle every 10 second.
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.SECONDS);
	}

	// show all entries into console. after first run show only fresh entry.
	public static void runEntry(int sourceCount, Map<String, Date> feedTimeMap, int i) {
		RSSReader reader = new RSSReader();
		ServletConnection connect = new ServletConnection();

		FeedMetaDataMenager menageData = new FeedMetaDataMenager();
		FreshEntryFinder compareDates = new FreshEntryFinder();
		try {
			Iterator<?> itEntries;
			// source name
			String name = menageData.readSourceName(i);
			// source URL
			String URL = menageData.readSourceURL(i);
			// source query method
			String method = menageData.getSourceQuery(URL);
			itEntries = reader.readRSSFeed(menageData.readSourceURL(i));
			if (itEntries != null) {
				// this block for first time run the code.
				if (feedTimeMap.get(name) == null) {
					SyndEntry entry = (SyndEntry) itEntries.next();
					connect.postRequest(entry.getTitle(), entry.getLink(), entry.getPublishedDate(), method);
					feedTimeMap.put(name, entry.getPublishedDate());
					while (itEntries.hasNext()) {
						entry = (SyndEntry) itEntries.next();
						connect.postRequest(entry.getTitle(), entry.getLink(), entry.getPublishedDate(), method);
					}
					// this block for after first run the code.
				} else {
					SyndEntry entry = (SyndEntry) itEntries.next();
					SyndEntry freshEntry = compareDates.compareDates(entry, feedTimeMap, name);
					if (freshEntry != null) {
						connect.postRequest(freshEntry.getTitle(), freshEntry.getLink(), freshEntry.getPublishedDate(),
								method);
						feedTimeMap.put(name, entry.getPublishedDate());
					}
					while (itEntries.hasNext()) {
						entry = (SyndEntry) itEntries.next();
						freshEntry = compareDates.compareDates(entry, feedTimeMap, name);
						if (freshEntry != null) {
							connect.postRequest(freshEntry.getTitle(), freshEntry.getLink(),
									freshEntry.getPublishedDate(), method);
						}
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(" array index out of bound  ", e);
		} catch (IOException e) {
			log.error("General I/O exception: ", e);
		} catch (IllegalArgumentException e) {
			log.error("illegal argument ", e);
		} catch (FeedException e) {
			log.error("feed is not valid", e);
		} catch (Exception e) {
			log.error("unexpected exception", e);
		}
	}
}