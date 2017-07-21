package crawler.DAO;

import java.sql.Connection;
import java.sql.SQLException;

import crawler.EmailCrawlerConfig;

public class CompanyDAO {

	private static Connection connection = MySQLConnector.createConnection("EmailCrawlerDB",
			EmailCrawlerConfig.getConfig().readString("db-username"),
			EmailCrawlerConfig.getConfig().readString("db-password"));

	public static void reconnect() throws SQLException {
		connection.close();
		connection = MySQLConnector.createConnection("EmailCrawlerDB",
				EmailCrawlerConfig.getConfig().readString("db-username"),
				EmailCrawlerConfig.getConfig().readString("db-password"));
	}

	public static void insert(String company_name, String company_domain) {
		MySQLConnector.executeQuery(connection,
				"INSERT INTO Company VALUES('" + company_name.replace("'", "''") + "', '" + company_domain + "');",
				true);
	}
	
	public static void updateHTML(String company_name, String HTML) {
		MySQLConnector.executeQuery(connection,
				"UPDATE `Company` SET `company_detail`='"+ HTML + "' WHERE company_name = '"+ company_name + "';",
				true);
	}
}