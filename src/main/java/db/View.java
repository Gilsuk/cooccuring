package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class View implements Iterator<Map<String,String>> {
	
	private ViewWorker worker;
	private int pointer, total;

	public View() {
		this.worker = ViewWorker.getInstance();
		this.pointer = 0;
		this.total = worker.getTotalCount();
	}

	@Override
	public boolean hasNext() {
		return pointer >= total ? false : true;
	}

	@Override
	public Map<String, String> next() {
		return worker.getRelWordsByWordId(++pointer);
	}
	
}

class ViewWorker {
	
	private Connection connection = null;
	private PreparedStatement selectStmt = null;
	private PreparedStatement selectCountStmt = null;

	private String selectCountState = new StringBuilder()
			.append("SELECT count(*) FROM keyword")
			.toString();

	private String selectState = new StringBuilder()
			.append("SELECT ")
			.append("	lt.word keyword,")
			.append("	rt.word relword,")
			.append("	lt.count count ")
			.append("FROM (")
			.append("	SELECT ")
			.append("		word,")
			.append("		rel_id id,")
			.append("		co.count ")
			.append("	FROM keyword ")
			.append("	JOIN cooccurence co ON id = key_id ")
			.append("	WHERE id = ? ")
			.append(") lt JOIN keyword rt ON lt.id = rt.id ")
			.append("ORDER BY count DESC ")
			.append("LIMIT 10")
			.toString();

	private static class Factory {
		public static final ViewWorker INSTANCE = new ViewWorker();
	}
	
	public static ViewWorker getInstance() {
		return Factory.INSTANCE;
	}

	private ViewWorker() {
		this.connection = Connector.getConnection();
		try {
			this.selectStmt = connection.prepareStatement(selectState);
			this.selectCountStmt = connection.prepareStatement(selectCountState);
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	int getTotalCount() {
		try {
			ResultSet resultSet = selectCountStmt.executeQuery();
			return resultSet.getInt(1);
		} catch (SQLException e) { e.printStackTrace(); }
		
		return 0;
	}
	
	Map<String, String> getRelWordsByWordId(int wordId) {
		String key = null;
		List<String> words = new ArrayList<>();
		Map<String, String> result = new HashMap<>();

		try {
			selectStmt.setInt(1, wordId);
			ResultSet resultSet = selectStmt.executeQuery();
			
			while (resultSet.next()) {
				if (key == null) key = resultSet.getString(1);
				words.add(resultSet.getString(2));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		result.put(key, words.toString());
		return result;
	}
}


