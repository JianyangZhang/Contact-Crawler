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
			WebDriverWait wait = new WebDriverWait(dr, 40);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'walkme-x-button')]")));
			dr.findElement(By.xpath("//div[contains(@class, 'walkme-x-button')]")).click();
		} catch (Exception e) {
			System.out.println("something wrong in method 'signInSalesgenie'");
		}
	}
	
	private String waitTillTableLoad() {
		WebDriverWait wait = new WebDriverWait(dr, 40);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("grid-view-table-container")));
		return dr.getPageSource();
	}
	
	protected void crawlCurrentTable() {
		String page = waitTillTableLoad();
		Document doc = Jsoup.parse(page);
		Elements elements_odd_tr = doc.select("tr.odd");
		Elements elements_even_tr = doc.select("tr.even");
		for (Element element : elements_odd_tr) {
			extractTableInfo(element);
		}
		for (Element element : elements_even_tr) {
			extractTableInfo(element);
		}
	}
	
	private void extractTableInfo(Element element) {
		if (element.children().size() > 10) {
			String companyName = element.select("div.BusinessName").text().trim();
			String personName = element.select("div.executive-name-field").text().trim();
			String phoneNumber = element.select("div[data-is-wireless]").attr("data-phone").trim();
			String street = element.select("td").get(8).text().trim();
			String city = element.select("td").get(9).text().trim();
			String state = element.select("td").get(10).text().trim();
			String zipCode = element.select("td").get(11).text().trim();
			if (!personName.equals("")) {
				System.out.print(companyName + " ");
				System.out.print(personName + " ");
				System.out.print(phoneNumber + " ");
				System.out.print(street + " ");
				System.out.print(city + " ");
				System.out.print(state + " ");
				System.out.println(zipCode + " ");
			}
		}
	}
}
