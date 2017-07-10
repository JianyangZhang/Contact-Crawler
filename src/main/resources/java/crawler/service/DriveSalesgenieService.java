package crawler.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import crawler.EmailCrawlerConfig;
import crawler.DAO.SalesgenieDAO;
import crawler.model.SalesGenieResult;

public class DriveSalesgenieService extends DriveBrowserService{
	private static final int WAIT_LONG = 30;
	private static final int WAIT_MEDIUM = 10;
	private static final int WAIT_SHORT = 3;

	DriveSalesgenieService() {
		super(Boolean.parseBoolean(EmailCrawlerConfig.getConfig().readString("show-gui")));
		this.dr.get("https://app.salesgenie.com/Login");
		// resize browser window to avoid element being invisible in no-GUI mode 
		if (!Boolean.parseBoolean(EmailCrawlerConfig.getConfig().readString("show-gui"))) {
			Dimension dimension = new Dimension(1920, 1080);
			this.dr.manage().window().setSize(dimension);
		}
	}
	
	/**
	 * Login Salesgenie
	 */
	protected void signInSalesgenie(String userName, String password) {
		WebDriverWait wait = new WebDriverWait(dr, WAIT_MEDIUM);
		dr.findElement(By.id("UserName")).sendKeys(userName);
		dr.findElement(By.id("Password")).sendKeys(password);
		dr.findElement(By.id("formSignIn")).click();
		try {
			dr.findElement(By.id("submit")).click();
		} catch (NoSuchElementException e) {
			System.out.println("is this your first time to sign in today?");
		} finally {}
		try {
			WebElement x = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'walkme-x-button')]"))).findElement(By.xpath("//div[contains(@class, 'walkme-x-button')]"));
			x.click();
		} catch (TimeoutException e) {
			System.out.println("the pop-up did not show up this time");
		} finally {}
	}
	
	/**
	 * search keywords
	 */
	protected void searchKeywords(String keywords) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.findElement(By.id("quickSearchUsBusiness")).click();
		WebDriverWait wait = new WebDriverWait(dr, WAIT_SHORT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("businessName")));
		dr.findElement(By.id("businessName")).sendKeys(keywords);
		dr.findElement(By.id("submit-quick-find")).click();
		try {
			waitTillTableLoad();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void waitTillTableLoad() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(dr, WAIT_LONG);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("grid-view-table-container")));
		Thread.sleep(3000);
	}
	
	private void waitTillTableRefresh(int timeOut) throws InterruptedException {
		String originText = dr.findElement(By.id("iconBarPageNo")).getText();
		String newText = originText;
		int flag = 0;
		do {
			flag++;
			Thread.sleep(1000);
			newText = dr.findElement(By.id("iconBarPageNo")).getText();
		} while (newText.equals(originText) && flag < timeOut);
		Thread.sleep(3000); // Salesgenie will load for a longer time without this line
	}
	
	protected ArrayList<SalesGenieResult> crawlSalesgenieResults(int count) {
		ArrayList<SalesGenieResult> resultList = new ArrayList<SalesGenieResult>();
		while (resultList.size() < count) {
			resultList.addAll(crawlCurrentTable());
			WebElement pageNextBtn = dr.findElement(By.xpath("//div[contains(@class, 'action-page-next')]"));
			if (pageNextBtn.isEnabled()) {
				pageNextBtn.click();
				try {
					waitTillTableRefresh(WAIT_LONG);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("no more result avaliable");
				break;
			}
		}
		return resultList;
	}
	
	private ArrayList<SalesGenieResult> crawlCurrentTable() {
		ArrayList<SalesGenieResult> resultList = new ArrayList<SalesGenieResult>();
		String page = dr.getPageSource();
		Document doc = Jsoup.parse(page);
		Elements elements_odd_tr = doc.select("tr.odd");
		Elements elements_even_tr = doc.select("tr.even");
		for (Element element : elements_odd_tr) {
			SalesGenieResult result = extractTableInfo(element);
			if (result != null) {
				resultList.add(result);
			}
		}
		for (Element element : elements_even_tr) {
			SalesGenieResult result = extractTableInfo(element);
			if (result != null) {
				resultList.add(result);
			}
		}
		return resultList;
	}
	
	private SalesGenieResult extractTableInfo(Element element) {
		SalesGenieResult result = null;
		if (element.children().size() > 10) { // there are many empty "tr" elements in this page for some reason
			String companyName = element.select("div.BusinessName").text().trim();
			String personName = element.select("div.executive-name-field").text().trim();
			String phoneNumber = element.select("div[data-is-wireless]").attr("data-phone").trim();
			String title = element.select("td").get(6).text().trim();
			String street = element.select("td").get(8).text().trim();
			String city = element.select("td").get(9).text().trim();
			String state = element.select("td").get(10).text().trim();
			String zipCode = element.select("td").get(11).text().trim();
			if (!personName.equals("") && !phoneNumber.equals("")) {
				result = new SalesGenieResult();
				System.out.print(companyName + " ");
				result.setCompanyName(companyName);
				System.out.print(personName + " ");
				result.setPersonName(personName);
				System.out.print(phoneNumber + " ");
				result.setPhoneNumber(phoneNumber);
				System.out.println(title + " ");
				result.setTitle(title);
				System.out.print(street + " ");
				result.setStreet(street);
				System.out.print(city + " ");
				result.setCity(city);
				System.out.print(state + " ");
				result.setState(state);
				System.out.println(zipCode + " ");
				result.setZipCode(zipCode);
				return result;
			}
		}
		return result;
	}
}
