package com.galaksiya.education.rss;

import java.io.IOException;
import java.text.ParseException;

import com.galaksiya.education.rss.executorService.ExecutorRunnerService;
import com.sun.syndication.io.FeedException;

public class ScheduledExecutorMain {

	public static void main(String[] args) throws ParseException, IllegalArgumentException, IOException, FeedException {
		new ExecutorRunnerService();
		ExecutorRunnerService.runExecute();
	}
}