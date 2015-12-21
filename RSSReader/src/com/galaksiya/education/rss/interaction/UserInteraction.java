package com.galaksiya.education.rss.interaction;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * ask user to what do you want. return 1 or 2;
 * 
 * @author galaksiya
 */
public class UserInteraction {
	private static UserInteraction instance = new UserInteraction();

	private String link;
	private String name;
	private String method;
	private int addOrRead;
	private Scanner scanner = new Scanner(System.in);

	public static UserInteraction getInstance() {
		return instance;
	}

	/**
	 * Ask to user for choose a option. get inputs from user * @throws
	 * IOException
	 */
	public void getUserPreferences() throws IOException {
		do {
			try {
				System.out.println("  select a news source  ---> 1\n" + "   Add new source       ---> 2\n");
				addOrRead = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("\nplease enter 1 or 2 ...\n");
				scanner.nextLine();
			}
			if (addOrRead == 2) {

				System.out.println("add a name ");
				scanner.nextLine();
				name = scanner.nextLine();
				System.out.println("add a new RSS URL: ");
				link = scanner.nextLine();
				do {
					System.out.println("add a HTML parse line: ");
					method = scanner.nextLine();
				} while (method.equals(""));
			}
		} while (addOrRead > 2 || addOrRead < 1);
	}

	public char addOrNot() {
		// asking user to add
		System.out.println("\nPress a for add this news source in the file :\n"
				+ "\nPress any button for continue without save...");
		return scanner.next().charAt(0);

	}

	public char addAlreadyExistRecord() {

		System.out
				.println("\n WARNING : \n  This record already exist,\n" + " press  y if  you want to add new one \n");
		return scanner.next().charAt(0);
	}

	public char continueCheck() {
		System.out.println("Press y for next feed :\n" + "Press any button for stop  :");
		char nextFeed = scanner.next().charAt(0);
		return nextFeed;
	}

	public String getLink() {
		return link;
	}

	public String getName() {
		return name;
	}

	public String getMethod() {
		return method;
	}

	public int getAddOrRead() {
		return addOrRead;
	}
}
