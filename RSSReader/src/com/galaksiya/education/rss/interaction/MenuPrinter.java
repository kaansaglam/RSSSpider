package com.galaksiya.education.rss.interaction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.galaksiya.education.rss.common.Info;

/**
 * show all news source..
 * 
 * @author galaksiya
 *
 */
public class MenuPrinter {

	private List<String> sourceNames;

	public MenuPrinter() throws FileNotFoundException, IOException {
		sourceNames = readSourceNames();
	}

	public int getSourceCount() {
		return sourceNames.size();
	}

	public int showMenu() throws IOException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		int newsNumber = 0;
		do {
			print();
			newsNumber = scanner.nextInt();
			scanner.nextLine();
		} while (newsNumber + 1 > getSourceCount() || newsNumber < 1);
		return newsNumber;
	}

	/**
	 * show all news sources name into console
	 * 
	 * @throws IOException
	 */
	public void print() {
		printSourceNames(sourceNames);
	}

	private void printSourceNames(List<String> sourceNames) {
		for (int i = 0; i < sourceNames.size(); i++) {
			System.out.println(sourceNames.get(i) + "  news  ---> " + i + "\n");
		}
	}

	private List<String> readSourceNames() throws IOException, FileNotFoundException {
		List<String> sourceNames = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(Info.RSS_FILE));) {
			String line = "";
			String csvSplitBy = ",";
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] sourcedata = line.split(csvSplitBy);
				if (sourcedata.length > 0) {
					sourceNames.add(sourcedata[0]);
				}
			}
		}
		return sourceNames;
	}
}
