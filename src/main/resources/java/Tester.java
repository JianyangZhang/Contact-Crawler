import java.io.File;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Tester {
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\jiany\\emailcrawlers\\chromedriver.exe");
		WebDriver dr = new ChromeDriver(enableExtension("C:\\\\Users\\jiany\\emailcrawlers\\hunter.crx"));
		dr.get("http://www.linkedin.com");
		// dr.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		switchTab("LinkedIn: Log In or Sign Up", dr);
		signInLinkedin("velozproject@gmail.com", "19940916", dr);
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
	 * Linkedin login 
	 */
	public static void signInLinkedin(String userName, String password, WebDriver dr){
		dr.findElement(By.name("session_key")).sendKeys(userName);
		dr.findElement(By.name("session_password")).sendKeys(password);
		dr.findElement(By.id("login-submit")).click();
	}
}