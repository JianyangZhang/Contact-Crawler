package crawler.DAO;

import java.sql.Connection;
import java.sql.ResultSet;

public class ResultDAO {

	private static Connection connection = MySQLConnector.createConnection("EmailCrawlerDB", "root", "");

	public static void insert(String search_id, String customer_linkedin_url) {
		MySQLConnector.executeQuery(connection,
				"INSERT INTO Result VALUES(" + "'" + search_id + "'" + ", " + "'" + customer_linkedin_url + "'" + ");",
				true);
	}
	
	public static ResultSet getAllBySearchID(String search_id) {
		return MySQLConnector.executeQuery(connection,
				"SELECT * FROM Result WHERE search_id = '" + search_id + "';",
				false);
	}
	
	public static ResultSet getAllByUrl(String customer_linkedin_url) {
		return MySQLConnector.executeQuery(connection,
				"SELECT * FROM Result WHERE customer_linkedin_url = '" + customer_linkedin_url + "';",
				false);
	}
}
