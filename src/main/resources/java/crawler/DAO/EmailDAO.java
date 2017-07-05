package crawler.DAO;

import java.sql.Connection;
import java.sql.SQLException;

import crawler.EmailCrawlerConfig;

public class EmailDAO {
	
	private static Connection connection = MySQLConnector.createConnection("EmailCrawlerDB", EmailCrawlerConfig.getConfig().readString("db-username"), EmailCrawlerConfig.getConfig().readString("db-password"));
	
	public static void reconnect() throws SQLException {
		connection.close();
		connection = MySQLConnector.createConnection("EmailCrawlerDB", EmailCrawlerConfig.getConfig().readString("db-username"), EmailCrawlerConfig.getConfig().readString("db-password"));
	}
	
	public static void insert(String email_address, String customer_linkedin_url, String company_name, int email_response_time) {
		MySQLConnector.executeQuery(connection, "INSERT INTO Email VALUES('" 
									+ email_address + "', '"
				                    + customer_linkedin_url + "', '"
				                    + company_name.replace("'", "''") + "', '" 
									+ email_response_time + "');", true);
	}
}
