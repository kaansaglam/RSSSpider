package com.galaksiya.education.rss.feed;

import java.io.IOException;
import java.util.Date;

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

	public SyndEntry isFresh(SyndEntry entry, Date lastFeedDate) throws FeedException, IOException {
		SyndEntry freshEntry = null;
		// compare entry date with last feed date...
		if (lastFeedDate != null && entry.getPublishedDate().after(lastFeedDate)) {
			freshEntry = entry;
		}

		return freshEntry;
	}
}
