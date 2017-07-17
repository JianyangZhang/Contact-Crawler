package crawler.service;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import crawler.EmailCrawlerConfig;

public class DriveBrowserService {

	protected static WebDriver dr;
	protected int screen_height;

	DriveBrowserService(boolean hasGUI) {
		if (hasGUI) {
			System.setProperty("webdriver.chrome.driver", EmailCrawlerConfig.getConfig().readString("chrome-driver-path"));
			this.dr = new ChromeDriver();
		} else {
			// this.dr = new HtmlUnitDriver(BrowserVersion.CHROME, true);
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setJavascriptEnabled(true);
			caps.setCapability("takesScreenshot", false);
			caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
					EmailCrawlerConfig.getConfig().readString("phantom-driver-path"));
			String[] args = new String[] {"--webdriver-loglevel=NONE"};
			caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, args);
			this.dr = new PhantomJSDriver(caps);
			Dimension size = this.dr.manage().window().getSize();
			screen_height = size.height;
		}
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
	 * navigate back
	 */
	protected void navBack() {
		JavascriptExecutor js = (JavascriptExecutor) dr;
		js.executeScript("window.history.go(-1)");
	}
	
	/**
	 * scroll current window to a specific position by pixel 
	 */
	protected synchronized void scrollTo(WebDriver driver, String position) throws InterruptedException {
		String oldpage = "";
		String newpage = "";
		do {
			oldpage = driver.getPageSource();
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0, " + position + ")");
			Thread.sleep(500);
			newpage = driver.getPageSource();
		} while (!oldpage.equals(newpage));
	}
}
