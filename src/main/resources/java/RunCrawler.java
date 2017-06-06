import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;

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
			// br.getPeopleInfo(dr);
			HashSet<String> urls = br.getPeopleUrl(dr);
			int flag = 0;
			for (String url : urls) {
				dr.get(url);
				extractInstitutionText(dr);
//				WebElement hunterButton = dr.findElement(By.xpath("//button[contains(@class, 'ehunter_linkedin_button')]"));
//				hunterButton.click();
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
			Thread.sleep(3000); // make sure hunter extension has been login successfully
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		dr.get("http://www.linkedin.com");
		br.switchTab("LinkedIn: Log In or Sign Up", dr);
		br.signInLinkedin("wangwent@usc.edu", "19940916".toCharArray(), dr);
	}
	
	/**
	 * extract company name/college name
	 * @throws InterruptedException 
	 */
	private static HashSet<String> extractInstitutionText(WebDriver dr) throws InterruptedException {
		HashSet<String> result = new HashSet<String>();
		JavascriptExecutor jse = (JavascriptExecutor) dr;
		jse.executeScript("scroll(0, 500);");
		jse.executeScript("scroll(0, 1000);");
		jse.executeScript("scroll(0, 1500);"); // to load all search results
		Thread.sleep(1500); // to fully load Linkedin page
		List<WebElement> webElements = dr.findElements(By.xpath("//section[contains(@class, 'experience-section')]//span[@class='pv-entity__secondary-title']"));
		// System.out.println(webElements.isEmpty());
		for (WebElement e : webElements) {
			System.out.println(e.getText());
			result.add(e.getText());
		}
		return result;
	}
}
