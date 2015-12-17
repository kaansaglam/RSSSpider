package com.galaksiya.education.writer;

import java.util.Date;

public class WriteMethodParameter {
	public String title;
	public String link;
	public Date date;
	public String method;
	public String filePath;

	public WriteMethodParameter(String title, String link, Date date, String method, String filePath) {
		this.title = title;
		this.link = link;
		this.date = date;
		this.method = method;
		this.filePath = filePath;
	}
}