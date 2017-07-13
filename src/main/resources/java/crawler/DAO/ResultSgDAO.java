package crawler.DAO;

import java.sql.Connection;
import java.sql.SQLException;

import crawler.EmailCrawlerConfig;

public class ResultSgDAO {
	private static Connection connection = MySQLConnector.createConnection("EmailCrawlerDB", EmailCrawlerConfig.getConfig().readString("db-username"), EmailCrawlerConfig.getConfig().readString("db-password"));
	
	public static void reconnect() throws SQLException {
		connection.close();
		connection = MySQLConnector.createConnection("EmailCrawlerDB", EmailCrawlerConfig.getConfig().readString("db-username"), EmailCrawlerConfig.getConfig().readString("db-password"));
	}
	
	public static void insert(String search_id, String name, String phone_num, String company) {
		MySQLConnector.executeQuery(connection,
				"INSERT INTO Result_SG VALUES('" + search_id + "', '" + name + "', '" + phone_num + "', '" + company + "');",
				true);
	}
}
