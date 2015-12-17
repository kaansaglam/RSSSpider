package com.galaksiya.education.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.galaksiya.education.writer.*;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class FeedWriterServlet extends HttpServlet {
	/**
	 * get json data from Post request. send the json content(title,link,date..)
	 * to writer class
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FeedWriterServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json;charset=UTF-8");

		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
		} finally {
			reader.close();
		}

		Gson gson = new Gson();
		Entry obj = gson.fromJson(sb.toString(), Entry.class);

		// String title = obj.title;
		// String link = obj.link;
		// String date = obj.date;
		// SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss
		// zzz yyyy");
		// Date dateParsed = null;
		// String filePath = null;
		// try {
		// dateParsed = format.parse(date);
		// } catch (ParseException e1) {
		// log.warn("date can not parse", e1);
		// }
		//
		// String method = obj.method.toString();
		EntryWriter write = new EntryWriter();

		try {
			write.writeFeedEntry(obj);
		} catch (ParseException e) {
			log.warn("parse -from POST request- is unvalid", e);
		}

	}

	public static class Entry {
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

		public String getDate() {
			return date;
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
}
