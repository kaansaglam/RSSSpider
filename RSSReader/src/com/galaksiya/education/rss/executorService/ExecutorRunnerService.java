package com.galaksiya.education.rss.executorService;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.galaksiya.education.rss.connection.ServletConnectionClient;
import com.galaksiya.education.rss.feed.FreshEntryFinder;
import com.galaksiya.education.rss.feed.RSSReader;
import com.galaksiya.education.rss.interaction.MenuPrinter;
import com.galaksiya.education.rss.metadata.FeedMetaDataManager;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;

public class ExecutorRunnerService {
	private static final Logger log = Logger.getLogger(ExecutorRunnerService.class);

	public static void runExecute() throws IOException {
		Map<String, Date> feedTimeMap = new HashMap<String, Date>();

		int sourceCount = new MenuPrinter().getSourceCount();
		try {
			// execute all rss source simultaneous
			ExecutorService executorService = Executors.newFixedThreadPool(sourceCount);

			Runnable runnable = new Runnable() {
				public void run() {
					for (int i = 1; i <= sourceCount; i++) {
						final int count = i;

						executorService.submit(new Runnable() {

							@Override
							public void run() {
								runEntry(feedTimeMap, count);
							}

						});
					}
				}
			};
			// run read cycle every 10 second.
			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
			service.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.SECONDS);
		} catch (IllegalArgumentException e) {
			log.error("could not find rss source", e);
		}

	}

	// send all entries on localhost. after first run, check only fresh entry.
	public static void runEntry(Map<String, Date> feedTimeMap, int sourceNumber) {

		RSSReader reader = new RSSReader();
		// connection to send data on localhost
		ServletConnectionClient connect = new ServletConnectionClient();
		FeedMetaDataManager menageData = new FeedMetaDataManager();
		FreshEntryFinder freshFinder = new FreshEntryFinder();
		try {
			Iterator<?> itEntries;
			// source name
			String name = menageData.readSourceName(sourceNumber);
			// source URL
			String URL = menageData.readSourceURL(sourceNumber);
			// source query method
			String method = menageData.getSourceQuery(URL);
			itEntries = reader.readRSSFeed(menageData.readSourceURL(sourceNumber));
			if (itEntries != null) {
				// this block for first time run the code.
				Date lastFeedDate = feedTimeMap.get(name);
				SyndEntry entry = (SyndEntry) itEntries.next();
				SyndEntry freshEntry = freshFinder.isFresh(entry, lastFeedDate);
				if (freshEntry == null && lastFeedDate == null) {
					connect.postRequest(entry.getTitle(), entry.getLink(), entry.getPublishedDate(), method);
					feedTimeMap.put(name, entry.getPublishedDate());
					while (itEntries.hasNext()) {
						entry = (SyndEntry) itEntries.next();
						connect.postRequest(entry.getTitle(), entry.getLink(), entry.getPublishedDate(), method);
					}
					// this block for after first run the code.
				} else {
					if (freshEntry != null) {
						connect.postRequest(freshEntry.getTitle(), freshEntry.getLink(), freshEntry.getPublishedDate(),
								method);
						// set last entry date
						feedTimeMap.put(name, entry.getPublishedDate());

						while (itEntries.hasNext()) {
							entry = (SyndEntry) itEntries.next();
							freshEntry = freshFinder.isFresh(entry, lastFeedDate);
							if (freshEntry != null) {
								connect.postRequest(freshEntry.getTitle(), freshEntry.getLink(),
										freshEntry.getPublishedDate(), method);
							}
						}
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(" array index out of bound  ", e);
		} catch (IOException e) {
			log.error("general I/O exception ", e);
		} catch (IllegalArgumentException e) {
			log.error("illegal argument ", e);
		} catch (FeedException e) {
			log.error("feed is not valid", e);
		} catch (Exception e) {
			log.error("unexpected exception", e);
		}
	}
}