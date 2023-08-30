package com.foxminded.schoolconsoleapp.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {
	private static final String PROPERTIES_FILE = "connection.properties";
	private static final Properties properties;

	static {
		properties = new Properties();
		try (InputStream inputStream = DataSource.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
			if (inputStream != null) {
				properties.load(inputStream);
			} else {
				throw new RuntimeException("Unable to find " + PROPERTIES_FILE);
			}
		} catch (IOException e) {
			throw new RuntimeException("Error loading " + PROPERTIES_FILE, e);
		}
	}

	public Connection createConnection() throws SQLException {
		String url = properties.getProperty("url");
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");

		return DriverManager.getConnection(url, username, password);
	}
}
