package crawler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailCrawlerConfig {
	private static final EmailCrawlerConfig CONFIG = new EmailCrawlerConfig();
	private EmailCrawlerConfig() {};
	public static EmailCrawlerConfig getConfig() {
		return CONFIG;
	}
	
	public static int readInt(String propName) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			System.out.println("config: " + propName + ": " + prop.getProperty(propName));
			return Integer.parseInt(prop.getProperty(propName));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return -1;
	}
	
	public static String readString(String propName) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			System.out.println("config: " + propName + ": " + prop.getProperty(propName));
			return prop.getProperty(propName);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
}