import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
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
	public static HashSet<String> domainSet;
/*
	public static void main(String[] args) throws IOException, InterruptedException {
		BrowserDriver br = new BrowserDriver();
		System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\jiany\\emailcrawlers\\chromedriver.exe");
		WebDriver dr = new ChromeDriver(br.enableExtension("C:\\\\Users\\jiany\\emailcrawlers\\hunter.crx"));
		System.setProperty("webdriver.chrome.driver", "/Applications/chromedriver");
		WebDriver dr = new ChromeDriver(br.enableExtension("/Users/Wentao/Documents/hunter.crx"));

		dr.get("http://www.linkedin.com");

		br.switchTab("LinkedIn: Log In or Sign Up", dr);
		br.signInLinkedin("wangwent@usc.edu", "19940916", dr);
		br.searchKeyword("google", dr);
		br.getPeopleInfo(dr);
	}
*/
	BrowserDriver() {
		domainSet = new HashSet<String>();
		domainSet.add("gmail.com");
		domainSet.add("yahoo.com");
		domainSet.add("hotmail.com");
		domainSet.add("outlook.com");
		domainSet.add("aol.com");
		domainSet.add("msn.com");
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
	protected boolean switchTab(String tabName, WebDriver dr) {
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
	 * login Hunter
	 */
	protected void signInHunter(String userName, String password, WebDriver dr) {
		dr.get("https://hunter.io/users/sign_in");	
		dr.findElement(By.id("email-field")).sendKeys(userName);
		dr.findElement(By.id("password-field")).sendKeys(password);
		WebElement loginButton = dr.findElement(By.xpath("//button[contains(@class, 'orange-btn')]"));
		loginButton.click();
	}

	/**
	 * login Linkedin
	 */
	protected void signInLinkedin(String userName, char[] password, WebDriver dr) {	
		dr.findElement(By.name("session_key")).sendKeys(userName);
		dr.findElement(By.name("session_password")).sendKeys(new String(password));
		dr.findElement(By.id("login-submit")).click();
	}

	/**
	 * search keyword in Linkedin
	 * 
	 * @throws InterruptedException
	 **/

	protected void searchKeyword(String title, WebDriver dr) throws InterruptedException {
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
	protected void getPeopleInfo(WebDriver dr) throws IOException, InterruptedException {
		String page = dr.getPageSource();
		Document doc = Jsoup.parse(page);
		Elements elements_name = doc.select("span.name.actor-name");
		Elements elements_title = doc.select("div.search-results__primary-cluster p.subline-level-1");

		Iterator iter_name = elements_name.iterator();
		Iterator iter_title = elements_title.iterator();

		while (iter_name.hasNext() && iter_title.hasNext()) {
			String name = ((Element) iter_name.next()).text().toLowerCase();
			String title = ((Element) iter_title.next()).text().toLowerCase();
			ArrayList<String> resultSet = guessEmail(name, title);
			for (String result : resultSet) {
				System.out.println("name: " + name + " | " + "title: " + title + " | " + "email: " + result);
			}
		}
	}

	/**
	 * guess email
	 */
	protected ArrayList<String> guessEmail(String name, String title) {
		String firstName = name.substring(0, name.indexOf(" "));
		String lastName = name.substring(name.indexOf(" ") + 1, name.length());
		char firstLetterOfLastName = lastName.charAt(0);
		ArrayList<String> result = new ArrayList<String>();
		for (String domain : domainSet) {
			result.add(firstName + "@" + domain);
			result.add(firstName + firstLetterOfLastName + "@" + domain);
			result.add(firstLetterOfLastName + firstName + "@" + domain);
			result.add(firstName + "." + lastName + "@" + domain);
			result.add(lastName + "." + firstName + "@" + domain);
		}
		return result;
	}

	/**
	 * get people's Linkedin url
	 */
	protected HashSet<String> getPeopleUrl(WebDriver dr) throws IOException, InterruptedException {
		HashSet<String> result = new HashSet<String>();
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
		return result;
	}
	
	/**
	 * close current tab
	 * */
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
}