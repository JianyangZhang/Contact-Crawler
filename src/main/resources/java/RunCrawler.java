import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashSet;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class RunCrawler {

	public static void main(String[] args) {

	}

	public static void basicSearch(String keyword) {
		// launch the browser driver
		BrowserDriver br = new BrowserDriver();
		System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\jiany\\emailcrawlers\\chromedriver.exe");
		WebDriver dr = new ChromeDriver(br.enableExtension("C:\\\\Users\\jiany\\emailcrawlers\\hunter.crx"));

		br.signInHunter("jianyang212@gmail.com", "zhang.3584", dr);
		try {
			Thread.sleep(3000); // make sure hunter extension has been login
								// successfully
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		dr.get("http://www.linkedin.com");
		br.switchTab("LinkedIn: Log In or Sign Up", dr);
		br.signInLinkedin("wangwent@usc.edu", "19940916".toCharArray(), dr);

		try {
			br.searchKeyword(keyword, dr);
			// br.getPeopleInfo(dr);
			HashSet<String> urls = br.getPeopleUrl(dr);
			int flag = 0;
			for (String url : urls) {
				dr.get(url);
				JavascriptExecutor jse = (JavascriptExecutor) dr;
				jse.executeScript("scroll(0, 1000);"); // to load all search results
				Thread.sleep(1500); // to fully load Linkedin page

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
}
