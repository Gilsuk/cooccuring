package db;

import java.sql.Connection;
import java.sql.SQLException;

public class Creator {
	
	private static final String createKeywordTableSQL = new StringBuilder()
			.append("CREATE TABLE keyword (")
			.append("  id INTEGER PRIMARY KEY,")
			.append("  word TEXT NOT NULL UNIQUE,")
			.append("  count INTEGER NOT NULL")
			.append(")")
			.toString();

	private static final String createCooccurenceTableSQL = new StringBuilder()
			.append("CREATE TABLE cooccurence (")
			.append("  key_id INTEGER,")
			.append("  rel_id INTEGER,")
			.append("  count INTEGER NOT NULL,")
			.append("    PRIMARY KEY (key_id, rel_id),")
			.append("    FOREIGN KEY (key_id)")
			.append("      REFERENCES keyword (id)")
			.append("        ON DELETE CASCADE")
			.append("        ON UPDATE NO ACTION,")
			.append("    FOREIGN KEY (rel_id)")
			.append("      REFERENCES keyword (id)")
			.append("        ON DELETE CASCADE")
			.append("        ON UPDATE NO ACTION")
			.append(")")
			.toString();
	
	public void createSchemes(Connection connection) {
		try {
			connection.prepareStatement(createKeywordTableSQL).execute();
			connection.prepareStatement(createCooccurenceTableSQL).execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}

}
