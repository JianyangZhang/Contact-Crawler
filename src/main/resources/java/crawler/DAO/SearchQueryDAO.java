package crawler.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import crawler.EmailCrawlerConfig;

public class SearchQueryDAO {
	
	private static Connection connection = MySQLConnector.createConnection("EmailCrawlerDB", EmailCrawlerConfig.getConfig().readString("db-username"), EmailCrawlerConfig.getConfig().readString("db-password"));
	
	public static void reconnect() throws SQLException {
		connection.close();
		connection = MySQLConnector.createConnection("EmailCrawlerDB", EmailCrawlerConfig.getConfig().readString("db-username"), EmailCrawlerConfig.getConfig().readString("db-password"));
	}
	
	public static ResultSet getPendingQuery() {
		return MySQLConnector.executeQuery(connection, "SELECT * FROM Search WHERE search_progress = 'pending' AND has_deleted = 0 ORDER BY search_id LIMIT 1;", false);
	}
	
	public static void updateToProcessing(String search_id) {
		MySQLConnector.executeQuery(connection, "UPDATE Search SET search_progress = 'processing' WHERE search_id = '" + search_id + "';", true);
	}
	
	public static void updateToComplete(String search_id) {
		MySQLConnector.executeQuery(connection, "UPDATE Search SET search_progress = 'completed' WHERE search_id = '" + search_id + "';", true);
	}
	
	public static void updateToFailed(String search_id) {
		MySQLConnector.executeQuery(connection, "UPDATE Search SET search_progress = 'completed' WHERE search_id = '" + search_id + "';", true);
	}
	public static ResultSet hasDeleted(String search_id) {
		return MySQLConnector.executeQuery(connection, "SELECT has_deleted FROM Search WHERE search_id = '" + search_id + "';", false);
	}
}
