package db;

import java.sql.Connection;
import java.sql.SQLException;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.JournalMode;
import org.sqlite.SQLiteConfig.SynchronousMode;

public class Connector {
	
	private Connector() {}
	
	private static class Factory {
		public static final Connection connection = new Connector().connect();
	}
	
	public static Connection getConnection() {
		return Factory.connection;
	}
	
	private Connection connect() {
		SQLiteConfig config = getConfig();
		return connectWithConfig(config);
	}

	private Connection connectWithConfig(SQLiteConfig config) {
		try {
			return config.createConnection("jdbc:sqlite::memory:");
		} catch (SQLException e) { e.printStackTrace(); }
		
		return null;
	}

	private SQLiteConfig getConfig() {
		SQLiteConfig config = new SQLiteConfig();
		config.setBusyTimeout(5000);
		config.setJournalMode(JournalMode.WAL);
		config.setSynchronous(SynchronousMode.OFF);
		config.enforceForeignKeys(true);
		return config;
	}
	
}
