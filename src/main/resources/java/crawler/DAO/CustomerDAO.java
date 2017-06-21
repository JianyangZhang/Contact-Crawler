package crawler.DAO;

import java.sql.Connection;

public class CustomerDAO {
	private static Connection connection = MySQLConnector.createConnection("EmailCrawlerDB", "lyihan", "900225");
	
	public static void insert(String customer_linkedin_url, String customer_name, String customer_title, String customer_location, String customer_keywords, String customer_create_time, String customer_touch_time, String internal_company_id, String compaign_step) {
		MySQLConnector.executeQuery(connection, "INSERT INTO Customer VALUES(" 
									+ "'" + customer_linkedin_url + "'" + ", " 
				                    + "'" + customer_name + "'" + ", "
				                    + "'" + customer_title + "'" + ", "
				                    + "'" + customer_location + "'" + ", "
				                    + "'" + customer_keywords + "'" + ", " 
				                    + "'" + customer_create_time + "'" + ", " 
				                    + "'" + customer_touch_time + "'" + ", " 
				                    + "'" + internal_company_id + "'" + ", "  
									+ "'" + compaign_step + "'" + ");", true);
	}
}
