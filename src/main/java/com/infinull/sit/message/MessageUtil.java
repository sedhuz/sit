package com.infinull.sit.message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.text.MessageFormat;

public class MessageUtil {
	private static final Properties  messages = new Properties();

	static {
		try (InputStream input = MessageUtil.class.getClassLoader().getResourceAsStream("sit-messages.properties")) {
			if (input == null) {
				System.err.println("Unable to find sit-messages.properties");
				System.exit(1);
			}
			messages.load(input);
		} catch (IOException e) {
			System.err.println("Error loading sit-messages.properties: " + e.getMessage());
			System.exit(1);
		}
	}

	public static String getMsg(String key, String... args) {
		String message = messages.getProperty(key);
		if (message == null) {
			return key;
		}
		if (args.length > 0) {
			return MessageFormat.format(message, (Object[]) args);
		}
		return message;
	}

	public static void printMsg(String key, String... args) {
		String message = getMsg(key, args);
		System.out.println(message);
	}
}