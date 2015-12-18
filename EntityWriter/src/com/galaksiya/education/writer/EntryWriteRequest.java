package com.galaksiya.education.writer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class EntryWriteRequest {
	
	private static final Logger logger = LogManager.getLogger(EntryWriteRequest.class);

	String title;
	String link;
	String date;
	String method;
	String filePath;

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public Date getDate() {
		// format the date value String to Date format
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		Date dateParsed = null;
		try {
			if (date != null) {

				dateParsed = format.parse(date);
			}
		} catch (ParseException e1) {
			logger.warn("could not parse date", e1);
		}
		return dateParsed;
	}

	public String getMethod() {
		return method;
	}

	public String getFilePath() {
		return filePath;
	}

	public String setTitle(String value) {
		return title = value;
	}

	public String setlink(String value) {
		return link = value;
	}

	public String setDate(String value) {
		return date = value;
	}

	public String setMethod(String value) {
		return method = value;
	}

	public String setFilePath(String value) {
		return filePath = value;
	}
}