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
		crawl("uber", 10);
	}

	public static ArrayList<Customer> crawl(String keyword, int count) {
		ArrayList<Customer> result = new ArrayList<Customer>();
		BrowserDriver br = new BrowserDriver();
		br.signInLinkedin("wangwent@usc.edu", "19940916".toCharArray());
		try {
			br.searchKeyword(keyword);
			HashSet<String> urls = br.getPeopleUrl(count);
			int flag = 0;
			for (String url : urls) {
				br.dr.get(url);
				String name = br.extractName();
				HashSet<String> institutionSet = br.extractInstitution();
				HashMap<String, String> domainMap = br.parseDomainFromGoogle(institutionSet);
				// WebElement hunterButton =
				// dr.findElement(By.xpath("//button[contains(@class,
				// 'ehunter_linkedin_button')]"));
				// hunterButton.click();
				Customer customer = new Customer(name, domainMap);
				System.out.println(name + "'s verified emails:");
				customer.getEmails();
				System.out.println("------------------------------------------");
				result.add(customer);
				flag++;
				if (flag == count) {
					break;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return result;
	}
}
