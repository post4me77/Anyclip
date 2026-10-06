package com.anyclip;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReadPropertyFile {
	private static final Properties prop = loadProperties();

	public ReadPropertyFile() {
	}

	private static Properties loadProperties() {
		Properties properties = new Properties();
		try (InputStream input = ReadPropertyFile.class.getResourceAsStream("/enviroment.properties")) {
			if (input == null) {
				throw new IllegalStateException("Missing test resource: enviroment.properties");
			}
			properties.load(input);
			return properties;
		} catch (IOException e) {
			throw new IllegalStateException("Cannot load enviroment.properties", e);
		}
	}
	
	public static List<Integer> getVallueWithComma(String value) {
		List<Integer> list = new ArrayList<Integer>();
		String configuredValue = prop.getProperty(value);
		if (configuredValue == null) {
			throw new IllegalArgumentException("Missing configuration property: " + value);
		}
		for (String item : configuredValue.split(",")) {
			list.add(Integer.parseInt(item.trim()));
		}
		return list;
	}
}
