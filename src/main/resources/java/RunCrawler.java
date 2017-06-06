import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
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

public class RunCrawler {
	private static WebDriver dr;
	private static BrowserDriver br;

	public static void main(String[] args) {
		basicSearch("uber");
	}

	public static void basicSearch(String keyword) {
		initializeCrawler();
		try {
			br.searchKeyword(keyword, dr);
			HashSet<String> urls = br.getPeopleUrl(dr);
			int flag = 0;
			for (String url : urls) {
				dr.get(url);
				HashSet<String> institutionSet = extractInstitutionText(dr);
				HashMap<String, HashSet<String>> domainMap = parseDomainFromGoogle(institutionSet);

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
	 * setup Chrome browser
	 **/
	private static void initializeCrawler() {
		// launch the browser driver
		br = new BrowserDriver();
		System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\jiany\\emailcrawlers\\chromedriver.exe");
		dr = new ChromeDriver(br.enableExtension("C:\\\\Users\\jiany\\emailcrawlers\\hunter.crx"));
		try {
			Thread.sleep(3000); // avoid tab closing before all tabs are loaded
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		br.closeTab();
		// br.signInHunter("jianyang212@gmail.com", "zhang.3584", dr);
		try {
			Thread.sleep(3000); // make sure hunter extension has been login
								// successfully
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		dr.get("http://www.linkedin.com");
		br.switchTab("LinkedIn: Log In or Sign Up", dr);
		br.signInLinkedin("wangwent@usc.edu", "19940916".toCharArray(), dr);
	}

	/**
	 * extract company name/college name
	 * 
	 * @throws InterruptedException
	 */
	private static HashSet<String> extractInstitutionText(WebDriver dr) throws InterruptedException {
		HashSet<String> result = new HashSet<String>();
		JavascriptExecutor jse = (JavascriptExecutor) dr;
		jse.executeScript("scroll(0, 500);");
		jse.executeScript("scroll(0, 1000);");
		jse.executeScript("scroll(0, 1500);"); // to load all search results
		Thread.sleep(1500); // to fully load Linkedin page
		List<WebElement> webElements = dr.findElements(By
				.xpath("//section[contains(@class, 'experience-section')]//span[@class='pv-entity__secondary-title']"));
		// System.out.println(webElements.isEmpty());
		for (WebElement e : webElements) {
			System.out.println(e.getText());
			result.add(e.getText());
		}
		return result;
	}

	/**
	 * find email domain from google search
	 * 
	 * @throws IOException
	 */
	private static HashMap<String, HashSet<String>> parseDomainFromGoogle(HashSet<String> institutionSet)
			throws IOException {
		HashMap<String, HashSet<String>> result = new HashMap<String, HashSet<String>>();
		int flag = 0;
		for (String s : institutionSet) {
			s = s.replace(" ", "+");
			String query = "https://www.google.com/search?q=" + s + "+official" + "+site";
			System.out.println("query: " + query);
			Elements links = Jsoup.connect(query)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0").get()
					.select(".g>.r>a");
			String title = links.first().text();
			String url = links.first().absUrl("href"); // Google returns URLs in
														// format
														// "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
			url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");
			String domain;
			try {
				domain = domainFromURL(url);
				System.out.println("Official Site Title: " + title);
				System.out.println("Official Site URL: " + url);
				System.out.println("Domain: " + domain);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			// flag++;
			if (flag == 1) {
				break;
			}
		}
		return result;
	}

	/**
	 * substring domain from url
	 */
	private static String domainFromURL(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
}
