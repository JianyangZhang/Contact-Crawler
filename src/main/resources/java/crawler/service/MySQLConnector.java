package crawler.service;
import java.sql.*;

/**
 * Connect to MySQL database and execute queries.
 **/
public class MySQLConnector {
	public static void main(String[] args) {
		// tester
		Connection connection = MySQLConnector.createConnection("transcript", "root", "");
		ResultSet resultSet = MySQLConnector.executeQuery(connection, "select * from student");
		MySQLConnector.printResultSet(resultSet);
	}

	public static Connection createConnection(String schema, String username, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + schema, username,
					password);
			return connection;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static ResultSet executeQuery(Connection connection, String query) {
		try {
			Statement stmt = connection.createStatement();
			return stmt.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void printResultSet(ResultSet resultSet) {
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnsNumber = metaData.getColumnCount();
			while (resultSet.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) {
						System.out.print(",  ");
					}
					String columnValue = resultSet.getString(i);
					System.out.print(metaData.getColumnName(i) + ": " + columnValue);
				}
				System.out.println("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
