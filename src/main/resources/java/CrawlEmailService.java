import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class CrawlEmailService {
	protected static BrowserDriver br;

	public static void main(String[] args) {
		crawl("uber");
	}

	public static void crawl(String keyword) {
		BrowserDriver br = new BrowserDriver();
		br.signInLinkedin("wangwent@usc.edu", "19940916".toCharArray());
		try {
			br.searchKeyword(keyword);
			HashSet<String> urls = br.getPeopleUrl();
			int flag = 0;
			for (String url : urls) {
				br.dr.get(url);
				HashSet<String> institutionSet = br.extractInstitutionText();
				HashMap<String, String> domainMap = br.parseDomainFromGoogle(institutionSet);
				// WebElement hunterButton =
				// dr.findElement(By.xpath("//button[contains(@class,
				// 'ehunter_linkedin_button')]"));
				// hunterButton.click();
				flag++;
				if (flag == 1) {
					break;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * guess email prefix
	 */
	private static ArrayList<String> guessPrefix(String name) {
		String firstName = name.substring(0, name.indexOf(" "));
		String lastName = name.substring(name.indexOf(" ") + 1, name.length());
		char lastName_initial = lastName.charAt(0);
		ArrayList<String> result = new ArrayList<String>();
		result.add(firstName);
		result.add(firstName + lastName);
		result.add(firstName + lastName_initial);
		result.add(lastName_initial + firstName);
		result.add(firstName + "." + lastName);
		result.add(lastName + "." + firstName);
		result.add(firstName + "_" + lastName);
		result.add(lastName + "_" + firstName);
		result.add(firstName + "-" + lastName);
		result.add(lastName + "-" + firstName);
		return result;
	}
}
