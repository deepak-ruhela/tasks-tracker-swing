package taskstrackerswing.util;

import java.sql.*;

public class DBHandler {
	private Connection connection;

	public Connection getConnection() {
		try {
			// Load the SQLite JDBC driver (you must include the SQLite JAR file in your
			// project)
			Class.forName("org.sqlite.JDBC");
			// Establish a connection to the database
			connection = DriverManager.getConnection("jdbc:sqlite:todoapp.db");
			// Create the users table if it doesn't exist
			Statement stmt = connection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS users (name TEXT PRIMARY KEY, password TEXT)";
			stmt.executeUpdate(sql);
			System.out.println("Connected to database and ensured users table exists.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

}
