package com.galaksiya.education.rss.feed;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;

/**
 * get a RSS entry and HashMaps. compare masp date and entry date. return entry
 * if entry is a fresh entry
 * 
 * @author galaksiya
 *
 */
public class FreshEntryFinder {

	public SyndEntry compareDates(SyndEntry entry, Map<String, Date> feedTimeMap, String name)
			throws FeedException, IOException {
		SyndEntry freshEntry = null;

		// compare last feed date with all feed dates
		// if new date is after old date then increase feed count.
		if (feedTimeMap.get(name) == null) {

		} else if (entry.getPublishedDate().after(feedTimeMap.get(name))) {
			freshEntry = entry;
		}

		return freshEntry;
	}
}
