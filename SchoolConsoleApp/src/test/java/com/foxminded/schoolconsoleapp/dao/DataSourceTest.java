package com.foxminded.schoolconsoleapp.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.Test;

class DataSourceTest {

	@Test
	void testConnectionProperties() {
		Properties properties = new Properties();
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("connection.properties")) {
			assertNotNull(inputStream, "connection.properties file not found");

			properties.load(inputStream);
		} catch (IOException e) {
			fail("Failed to load connection.properties", e);
		}

		String url = properties.getProperty("url");
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");

		assertNotNull(url, "Missing 'url' property in connection.properties");
		assertNotNull(username, "Missing 'username' property in connection.properties");
		assertNotNull(password, "Missing 'password' property in connection.properties");
	}

}
