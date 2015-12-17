package com.galaksiya.education.rss.interaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.galaksiya.education.rss.common.Info;

/**
 * show all news source and ask user "which one?".
 * 
 * @author galaksiya
 *
 */
public class MenuPrinter {
	public int showMenu() throws IOException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		int newsNumber = 0;
		int newsCount = 0;
		do {

			newsCount = getConsoleText();
			newsNumber = scanner.nextInt();
			scanner.nextLine();
		} while (newsNumber + 1 > newsCount || newsNumber < 1);
		return newsNumber;
	}

	/**
	 * show all news sources name into console
	 * 
	 * @throws IOException
	 */
	public int getConsoleText() throws IOException {

		String line = "";
		String cvsSplitBy = ",";
		BufferedReader br = new BufferedReader(new FileReader(Info.RSS_FILE));
		int newsNumber = 1;
		while ((line = br.readLine()) != null) {
			// use comma as separator
			String[] CSVdata = line.split(cvsSplitBy);
			System.out.println(CSVdata[0] + "  news  ---> " + newsNumber + "\n");
			newsNumber++;
		}
		br.close();
		return newsNumber;

	}

}
