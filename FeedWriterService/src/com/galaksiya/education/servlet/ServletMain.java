package com.galaksiya.education.servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class ServletMain {
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		ServletContextHandler handler = new ServletContextHandler(server, "/*");
		handler.addServlet(FeedWriterServlet.class, "/feeds");
		server.start();

	}

}
