package com.galaksiya.education.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.galaksiya.education.writer.*;

import com.google.gson.Gson;

public class FeedWriterServlet extends HttpServlet {
	/**
	 * get json data from Post request. send the json content in a object and
	 * send it to writer class
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json;charset=UTF-8");
		// read json content..
		StringBuilder sb = new StringBuilder();

		try (BufferedReader reader = request.getReader();) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
		}
		Gson gson = new Gson();
		// set json content as a object
		EntryWriteRequest obj = gson.fromJson(sb.toString(), EntryWriteRequest.class);
		// call writer method.
		new EntryWriter().writeFeedEntry(obj);
	}
}
