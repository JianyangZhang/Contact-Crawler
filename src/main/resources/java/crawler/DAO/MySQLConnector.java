package crawler.DAO;
import java.sql.*;
import java.text.SimpleDateFormat;

import crawler.EmailCrawlerConfig;

/**
 * Connect to MySQL database and execute queries.
 **/
public class MySQLConnector {
	public static Connection createConnection(String schema, String username, String password) {
		try {
			Class.forName(EmailCrawlerConfig.getConfig().readString("db-driver"));
			Connection connection = DriverManager.getConnection(EmailCrawlerConfig.getConfig().readString("db-path") + schema, username,
					password);
			return connection;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static ResultSet executeQuery(Connection connection, String query, boolean isUpdate) {
		try {
			Statement stmt = connection.createStatement();
			if (isUpdate) {
				stmt.executeUpdate(query);
				return null;
			} else {
				return stmt.executeQuery(query);
			}
		} catch (Exception e) {
			if (e instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException) {
				// System.out.println("warning: tried to insert duplicate primary key, skipped and continue.");
				e.printStackTrace();
			} else {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void printResultSet(ResultSet resultSet) {
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnsNumber = metaData.getColumnCount();
			System.out.println("TIMESTAMP." + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
			resultSet.beforeFirst();
			while (resultSet.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) {
						System.out.print(",  ");
					}
					String columnValue = resultSet.getString(i);
					System.out.print(metaData.getColumnName(i) + ": " + columnValue);
				}
				System.out.println("");
				System.out.println("------------------------------------------");
			}
			resultSet.beforeFirst();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
