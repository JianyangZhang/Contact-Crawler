package crawler.DAO;

import java.sql.Connection;

public class ResultDAO {

	private static Connection connection = MySQLConnector.createConnection("EmailCrawlerDB", "root", "");

	public static void insert(String search_id, String customer_linkedin_url) {
		MySQLConnector.executeQuery(connection,
				"INSERT INTO Result VALUES(" + "'" + search_id + "'" + ", " + "'" + customer_linkedin_url + "'" + ");",
				true);
	}
}
