package crawler.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import crawler.EmailCrawlerConfig;

public class SalesgenieDAO {
	private static Connection connection = MySQLConnector.createConnection("EmailCrawlerDB", EmailCrawlerConfig.getConfig().readString("db-username"), EmailCrawlerConfig.getConfig().readString("db-password"));
	
	public static void reconnect() throws SQLException {
		connection.close();
		connection = MySQLConnector.createConnection("EmailCrawlerDB", EmailCrawlerConfig.getConfig().readString("db-username"), EmailCrawlerConfig.getConfig().readString("db-password"));
	}
	public static void insert(String sg_person_name, String sg_phone_number, String sg_title, String sg_company_name, String sg_street, String sg_city, String sg_state, String sg_zipcode) {
		MySQLConnector.executeQuery(connection, "INSERT INTO SalesGenie VALUES('" 
			+ sg_person_name + "', '" 
			+ sg_phone_number + "', '"
			+ sg_title + "', '"
			+ sg_company_name + "', '"
			+ sg_street + "', '"
			+ sg_city + "', '" 
			+ sg_state + "', '" 
			+ sg_zipcode + "', '');", true);
	}

	public static ResultSet getSGNamesAll() {
		return MySQLConnector.executeQuery(connection, "SELECT `sg_person_name`, `sg_company_name`, `sg_title` FROM `SalesGenie` WHERE `email`='';", false);
	}
	
	public static void updateEmail(String name, String company, String title, String email) {
		MySQLConnector.executeQuery(connection, "UPDATE `SalesGenie` SET `email`='"
				+ email.replace("'", "''") + "' WHERE `sg_person_name`='"
				+ name.replace("'", "''") + "' and `sg_company_name`='"
				+ company.replace("'", "''") + "' and `sg_title`='"
				+ title + "';", true);
		
	}
}
