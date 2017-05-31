import java.util.Iterator;
import java.util.List;
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
	public static void main(String[] args) throws IOException, InterruptedException {
		System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\jiany\\emailcrawlers\\chromedriver.exe");
		WebDriver dr = new ChromeDriver(enableExtension("C:\\\\Users\\jiany\\emailcrawlers\\hunter.crx"));
		// System.setProperty("webdriver.chrome.driver",
		// "/Applications/chromedriver");
		// WebDriver dr = new
		// ChromeDriver(enableExtension("/Users/Wentao/Documents/hunter.crx"));

		dr.get("http://www.linkedin.com");
		switchTab("LinkedIn: Log In or Sign Up", dr);
		signInLinkedin("wangwent@usc.edu", "19940916", dr);
		searchKeyword("facebook", dr);
	}

	/**
	 * use extension in the Chrome browser
	 **/
	public static DesiredCapabilities enableExtension(String path) {
		ChromeOptions options = new ChromeOptions();
		options.addExtensions(new File(path));
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		return capabilities;
	}

	/**
	 * switch tab based on tab name
	 */
	public static boolean switchTab(String tabName, WebDriver dr) {
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
	public static void signInHunter(String userName, String password, WebDriver dr) {
		dr.findElement(By.name("session_key")).sendKeys(userName);
		dr.findElement(By.name("session_password")).sendKeys(password);
		dr.findElement(By.id("login-submit")).click();
	}

	/**
	 * login Linkedin
	 */
	public static void signInLinkedin(String userName, String password, WebDriver dr) {
		dr.findElement(By.name("session_key")).sendKeys(userName);
		dr.findElement(By.name("session_password")).sendKeys(password);
		dr.findElement(By.id("login-submit")).click();
	}

	/**
	 * input the search keyword and get page source
	 * 
	 * @throws InterruptedException
	 */
	public static void searchKeyword(String title, WebDriver dr) throws IOException, InterruptedException {
		String url = "https://www.linkedin.com/search/results/index/?keywords=" + title
				+ "&origin=GLOBAL_SEARCH_HEADER";
		dr.get(url);
		JavascriptExecutor jse = (JavascriptExecutor) dr;
		jse.executeScript("scroll(0, 1000);");

		String page = dr.getPageSource();
		System.out.println(page);

		Document doc = Jsoup.parse(page);
		Elements uls = doc.getElementsByTag("ul");
		for (Element ul : uls) {
			Elements lists = ul.getElementsByTag("li");
			for (Element li : lists) {
				try {
					Elements aTags = li.getElementsByTag("a");
					for (Element aTag : aTags) {
						String href = aTag.attr("href");
						System.out.println("href is " + href);
					}
				} catch (Exception e) {

				}
			}
		}
	}
}