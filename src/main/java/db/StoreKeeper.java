package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StoreKeeper {
	
	private Connection connection = null;
	private PreparedStatement selectCountStmt = null;
	private PreparedStatement insertStmt = null;
	private PreparedStatement updateStmt = null;
	private PreparedStatement selectPairCountStmt = null;
	private PreparedStatement insertPairStmt = null;
	private PreparedStatement updatePairStmt = null;
	private PreparedStatement selectIdStmt = null;
	
	private String selectKeywordCountState = new StringBuilder()
			.append("SELECT count(*) FROM keyword ")
			.append("WHERE word = ?")
			.toString();

	private String insertKeywordState = new StringBuilder()
			.append("INSERT INTO keyword (word, count) ")
			.append("VALUES (?, 1)")
			.toString();
	
	private String updateKeywordState = new StringBuilder()
			.append("UPDATE keyword SET count = (count + 1) ")
			.append("WHERE word = ?")
			.toString();
	
	private String selectPairCountState = new StringBuilder()
			.append("SELECT count(*) FROM cooccurence ")
			.append("WHERE key_id = ? AND rel_id = ?")
			.toString();

	private String insertPairState = new StringBuilder()
			.append("INSERT INTO cooccurence (key_id, rel_id, count) ")
			.append("VALUES (?, ?, 1)")
			.toString();
	
	private String updatePairState = new StringBuilder()
			.append("UPDATE cooccurence SET count = (count + 1) ")
			.append("WHERE key_id = ? AND rel_id = ?")
			.toString();

	private String selectIdState = new StringBuilder()
			.append("SELECT id FROM keyword ")
			.append("WHERE word = ?")
			.toString();

	private static class Factory {
		public static final StoreKeeper INSTANCE = new StoreKeeper();
	}
	
	public static StoreKeeper getInstance() {
		return Factory.INSTANCE;
	}

	private StoreKeeper() {
		this.connection = Connector.getConnection();
		try {
			this.selectCountStmt = connection.prepareStatement(selectKeywordCountState);
			this.insertStmt = connection.prepareStatement(insertKeywordState);
			this.updateStmt = connection.prepareStatement(updateKeywordState);
			this.selectPairCountStmt= connection.prepareStatement(selectPairCountState);
			this.insertPairStmt = connection.prepareStatement(insertPairState);
			this.updatePairStmt = connection.prepareStatement(updatePairState);
			this.selectIdStmt = connection.prepareStatement(selectIdState);
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public void store(List<String> nouns) {
		
		List<String> distinctedNouns = distinct(nouns);
		if (distinctedNouns.size() < 2) return;
		
		for (String noun : distinctedNouns)
			store(noun);
		
		List<Integer> ids = mapToId(distinctedNouns);
		
		List<int[]> pairs = toPairs(ids);
		
		for (int[] pair : pairs)
			store(pair);

	}

	private void store(String keyword) {
		try {
			if (isExist(keyword))
				update(keyword);
			else
				insert(keyword);

		} catch (SQLException e) { e.printStackTrace(); }
	}

	private void store(int[] pair) {
		try {
			if (isExist(pair))
				update(pair);
			else
				insert(pair);

		} catch (SQLException e) { e.printStackTrace(); }
	}

	private List<Integer> mapToId(List<String> distinctedNouns) {
		return distinctedNouns.stream().map(this::toWordId).collect(Collectors.toList());
	}
	
	private int toWordId(String keyword) {
		int id = 0;
		try {
			selectIdStmt.setString(1, keyword);
			ResultSet resultSet = selectIdStmt.executeQuery();
			id = resultSet.getInt(1);
		} catch (SQLException e) { e.printStackTrace(); }

		return id;
	}

	private List<String> distinct(List<String> nouns) {
		return nouns.stream().distinct().collect(Collectors.toList());
	}
	
	private List<int[]> toPairs(List<Integer> ids) {
		
		List<int[]> result = new ArrayList<>();
		
		for (int keyid : ids)
			for (int relid : ids) {
				if (keyid == relid) continue;
				result.add(new int[] {keyid, relid});
			}
				
		return result;
	}
	
	private boolean isExist(String keyword) throws SQLException {
		selectCountStmt.setString(1, keyword);
		ResultSet selectResult = selectCountStmt.executeQuery();
		int count = selectResult.getInt(1);

		return count == 0 ? false : true;
	}
	
	private void update(String keyword) throws SQLException {
		updateStmt.setString(1, keyword);
		updateStmt.executeUpdate();
	}
	
	private void insert(String keyword) throws SQLException {
		insertStmt.setString(1, keyword);
		insertStmt.executeUpdate();
	}

	private boolean isExist(int[] pair) throws SQLException {
		selectPairCountStmt.setInt(1, pair[0]);
		selectPairCountStmt.setInt(2, pair[1]);
		ResultSet selectResult = selectPairCountStmt.executeQuery();
		int count = selectResult.getInt(1);

		return count == 0 ? false : true;
	}

	private void update(int[] pair) throws SQLException {
		updatePairStmt.setInt(1, pair[0]);
		updatePairStmt.setInt(2, pair[1]);
		updatePairStmt.executeUpdate();
	}

	private void insert(int[] pair) throws SQLException {
		insertPairStmt.setInt(1, pair[0]);
		insertPairStmt.setInt(2, pair[1]);
		insertPairStmt.executeUpdate();
	}

}
