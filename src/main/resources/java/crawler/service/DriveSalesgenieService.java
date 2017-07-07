package crawler.service;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriveSalesgenieService extends DriveBrowserService{

	DriveSalesgenieService(boolean hasGUI) {
		super(hasGUI);
		this.dr.get("https://www.salesgenie.com/");
	}
	
	/**
	 * Login Salesgenie
	 */
	protected void signInSalesgenie(String userName, String password) {
		WebElement signInButton = dr.findElement(By.xpath("//li[contains(@class, 'pre-auth')]"));
		signInButton.click();
		dr.findElement(By.id("UserName")).sendKeys(userName);
		dr.findElement(By.id("Password")).sendKeys(password);
		dr.findElement(By.id("formSignIn")).click();
		try {
			dr.findElement(By.id("submit")).click();
			Thread.sleep(4000);
			WebElement skipAlert = dr.findElement(By.xpath("//div[contains(@class, 'walkme-x-button')]"));
			skipAlert.click();
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}
	
	private String waitCurrentTableLoad() {
		WebDriverWait wait = new WebDriverWait(dr, 40);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("grid-view-table-container")));
		return dr.getPageSource();
	}
	
	protected void crawlCurrentTable() {
		String page = waitCurrentTableLoad();
		Document doc = Jsoup.parse(page);
		Elements elements_odd_tr = doc.select("tr.odd");
		Elements elements_even_tr = doc.select("tr.even");
		for (Element element : elements_odd_tr) {
			String companyName = element.select("div.BusinessName").text().trim();
			if (!companyName.equals("")) {
				System.out.println(companyName);
			}
		}
		for (Element element : elements_even_tr) {
			String companyName = element.select("div.BusinessName").text().trim();
			if (!companyName.equals("")) {
				System.out.println(companyName);
			}
		}
	}
}
