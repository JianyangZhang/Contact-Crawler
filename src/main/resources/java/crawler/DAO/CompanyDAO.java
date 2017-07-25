package crawler.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
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
		ResultSet result = MySQLConnector.executeQuery(connection,
				"Select * From Company Where company_name='" + company_name.replace("'", "''") + "';",
				false);
		try {
			if(result.next()) {
				MySQLConnector.executeQuery(connection,
						"UPDATE Company Set company_domain = '" + company_domain + "' Where company_name ='" + company_name.replace("'", "''") + "';",
						true);
			}
			else {
				MySQLConnector.executeQuery(connection,
						"INSERT INTO Company VALUES('" + company_name.replace("'", "''") + "', '" + company_domain + "','');",
						true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateHTML(String company_name, String HTML) {
		ResultSet result = MySQLConnector.executeQuery(connection,
				"Select * From Company Where company_name='" + company_name.replace("'", "''") + "';",
				false);
		try {
			if(result.next()) {
				MySQLConnector.executeQuery(connection,
						"UPDATE `Company` SET `company_detail`='"+ HTML.replace("'", "''") + "' WHERE `company_name` = '"+ company_name.replace("'", "''") + "';",
						true);
			}
			else {
				MySQLConnector.executeQuery(connection,
						"INSERT INTO Company VALUES('" + company_name.replace("'", "''") + "', '','" + HTML.replace("'", "''") + "');",
						true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}