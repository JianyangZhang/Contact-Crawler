package crawler.DAO;

import java.sql.Connection;

public class EmailDAO {
	
	private static Connection connection = MySQLConnector.createConnection("EmailCrawlerDB", "root", "");
	
	public static void insert(String email_address, String customer_linkedin_url, String company_name, int email_response_time) {
		MySQLConnector.executeQuery(connection, "INSERT INTO Email VALUES(" 
									+ "'" + email_address + "'" + ", "
				                    + "'" + customer_linkedin_url + "'" + ", "
				                    + "'" + company_name + "'" + ", " 
									+ "'" + email_response_time + "'" + ");", true);
	}
}
