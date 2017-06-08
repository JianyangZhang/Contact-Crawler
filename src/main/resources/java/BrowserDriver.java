import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BrowserDriver {
	protected static HashSet<String> domainSet;
	protected static ChromeDriver dr;
	/*
	 * public static void main(String[] args) throws IOException,
	 * InterruptedException { BrowserDriver br = new BrowserDriver();
	 * System.setProperty("webdriver.chrome.driver",
	 * "C:\\\\Users\\jiany\\emailcrawlers\\chromedriver.exe"); WebDriver dr =
	 * new ChromeDriver(br.enableExtension(
	 * "C:\\\\Users\\jiany\\emailcrawlers\\hunter.crx"));
	 * System.setProperty("webdriver.chrome.driver",
	 * "/Applications/chromedriver"); WebDriver dr = new
	 * ChromeDriver(br.enableExtension("/Users/Wentao/Documents/hunter.crx"));
	 * 
	 * dr.get("http://www.linkedin.com");
	 * 
	 * br.switchTab("LinkedIn: Log In or Sign Up", dr);
	 * br.signInLinkedin("wangwent@usc.edu", "19940916", dr);
	 * br.searchKeyword("google", dr); br.getPeopleInfo(dr); }
	 */
	BrowserDriver() {
		// domainSet = new HashSet<String>();
		// domainSet.add("gmail.com");
		// domainSet.add("yahoo.com");
		// domainSet.add("hotmail.com");
		// domainSet.add("outlook.com");
		// domainSet.add("aol.com");
		// domainSet.add("msn.com");
		System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\jiany\\emailcrawlers\\chromedriver.exe");
		this.dr = new ChromeDriver();
		// dr = new
		// ChromeDriver(br.enableExtension("C:\\\\Users\\jiany\\emailcrawlers\\hunter.crx"));
		// try {
		// Thread.sleep(3000); // avoid tab closing before all tabs are loaded
		// } catch (Exception exception) {
		// exception.printStackTrace();
		// }
		// br.closeTab();
		// br.signInHunter("jianyang212@gmail.com", "zhang.3584", dr);
		// try {
		// Thread.sleep(3000); // make sure hunter extension has been login
		// // successfully
		// } catch (Exception exception) {
		// exception.printStackTrace();
		// }
		dr.get("http://www.linkedin.com");
		// br.switchTab("LinkedIn: Log In or Sign Up", dr);
	}

	/**
	 * use extension in the Chrome browser
	 **/
	protected DesiredCapabilities enableExtension(String path) {
		ChromeOptions options = new ChromeOptions();
		options.addExtensions(new File(path));
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		return capabilities;
	}

	/**
	 * switch tab based on tab name
	 */
	protected boolean switchTab(String tabName) {
		boolean flag = false;
		try {
			// save all tabs into a Set
			String currentHandle = dr.getWindowHandle();
			Set<String> handles = dr.getWindowHandles();
			// iterate the tabs Set to find the target tab
			for (String handle : handles) {
				dr.switchTo().window(handle);
				if (dr.getTitle().equals(tabName)) {
					break;
				} else {
					dr.switchTo().window(currentHandle);
				}
			}
		} catch (Exception e) {
			System.out.printf("Tab: " + tabName + " cound not be found!", e.fillInStackTrace());
			flag = false;
		}
		return flag;
	}

	/**
	 * close current tab
	 */
	protected void closeTab() {
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_W);
			robot.keyRelease(KeyEvent.VK_W);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		} catch (AWTException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Linkedin page turn
	 */
	private void pageTurn(int page) {
		String targetURL = dr.getCurrentUrl() + "&page=" + page;
		dr.get(targetURL);
	}

	/**
	 * login Hunter
	 */
	protected void signInHunter(String userName, String password) {
		dr.get("https://hunter.io/users/sign_in");
		dr.findElement(By.id("email-field")).sendKeys(userName);
		dr.findElement(By.id("password-field")).sendKeys(password);
		WebElement loginButton = dr.findElement(By.xpath("//button[contains(@class, 'orange-btn')]"));
		loginButton.click();
	}

	/**
	 * login Linkedin
	 */
	protected void signInLinkedin(String userName, char[] password) {
		dr.findElement(By.name("session_key")).sendKeys(userName);
		dr.findElement(By.name("session_password")).sendKeys(new String(password));
		dr.findElement(By.id("login-submit")).click();
	}

	/**
	 * search keyword in Linkedin
	 * 
	 * @throws InterruptedException
	 **/
	protected void searchKeyword(String title) throws InterruptedException {
		String url = "https://www.linkedin.com/search/results/index/?keywords=" + title
				+ "&origin=GLOBAL_SEARCH_HEADER";
		dr.get(url);
		JavascriptExecutor jse = (JavascriptExecutor) dr;
		jse.executeScript("scroll(0, 500);");
		jse.executeScript("scroll(0, 1000);"); // to load all search results
		Thread.sleep(1500); // to fully load Linkedin page
	}

	/**
	 * get people's basic info
	 */
	protected void getPeopleInfo() throws IOException, InterruptedException {
		String page = dr.getPageSource();
		Document doc = Jsoup.parse(page);
		Elements elements_name = doc.select("span.name.actor-name");
		Elements elements_title = doc.select("div.search-results__primary-cluster p.subline-level-1");

		Iterator iter_name = elements_name.iterator();
		Iterator iter_title = elements_title.iterator();

		while (iter_name.hasNext() && iter_title.hasNext()) {
			String name = ((Element) iter_name.next()).text();
			String title = ((Element) iter_title.next()).text();
			System.out.println("name: " + name + " | " + "title: " + title);
		}
	}

	/**
	 * get people's Linkedin url
	 */
	protected HashSet<String> getPeopleUrl(int count) throws IOException, InterruptedException {
		int currentPage = 1;
		HashSet<String> result = new HashSet<String>();
		while (result.size() < count) {
			String page = dr.getPageSource();
			Document doc = Jsoup.parse(page);
			Elements uls = doc.getElementsByTag("ul");
			for (Element ul : uls) {
				Elements lists = ul.getElementsByTag("li");
				for (Element li : lists) {
					try {
						Elements aTags = li.getElementsByTag("a");
						for (Element aTag : aTags) {
							String href = aTag.attr("href");
							if (href.startsWith("/in/")) {
								result.add("https://www.linkedin.com" + href);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("url set size: " + result.size());
			pageTurn(++currentPage);
		}
		return result;
	}
	
	/**
	 * extract name
	 */
	protected String extractName() {
		return dr.findElement(By.xpath("//h1[contains(@class, 'pv-top-card-section__name')]")).getText();
	}

	/**
	 * extract company name/college name
	 */
	protected HashSet<String> extractInstitution() throws InterruptedException {
		HashSet<String> result = new HashSet<String>();
		JavascriptExecutor jse = (JavascriptExecutor) dr;
		jse.executeScript("scroll(0, 500);");
		jse.executeScript("scroll(0, 1000);");
		jse.executeScript("scroll(0, 1500);");
		jse.executeScript("scroll(0, 2000);"); // to load all search results
		Thread.sleep(1500); // to fully load Linkedin page
		List<WebElement> webElements = dr.findElements(By
				.xpath("//section[contains(@class, 'experience-section')]//span[@class='pv-entity__secondary-title']"));
		// System.out.println(webElements.isEmpty());
		for (WebElement e : webElements) {
			result.add(e.getText().toLowerCase());
		}
		return result;
	}

	/**
	 * find email domain from google search
	 * @throws IOException
	 */
	protected HashMap<String, String> parseDomainFromGoogle(HashSet<String> institutionSet) throws IOException {
		HashMap<String, String> result = new HashMap<String, String>();
		int flag = 0;
		for (String s : institutionSet) {
			String query = "https://www.google.com/search?q=" + s.replace(" ", "+") + "+official" + "+site";
//			System.out.println("query: " + query);
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
//				System.out.println("Official Site Title: " + title);
//				System.out.println("Official Site URL: " + url);
//				System.out.println("Domain: " + domain);
				result.put(s, domain);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * substring domain from url
	 */
	private String domainFromURL(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
}