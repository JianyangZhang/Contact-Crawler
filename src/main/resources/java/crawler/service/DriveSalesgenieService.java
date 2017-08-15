package crawler.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import crawler.EmailCrawlerConfig;
import crawler.DAO.CompanyDAO;
import crawler.DAO.SalesgenieDAO;
import crawler.model.SalesGenieResult;

public class DriveSalesgenieService extends DriveBrowserService{
	private static final int WAIT_LONG = 30;
	private static final int WAIT_MEDIUM = 10;
	private static final int WAIT_SHORT = 5;

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
	protected void searchSIC(String code, String page) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.findElement(By.id("buildListUsBusiness")).click();
		
		WebDriverWait wait = new WebDriverWait(dr, WAIT_LONG);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("critLineOfBusinessSic")));
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.findElement(By.id("critLineOfBusinessSic")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ui-accordion-lineOfBusiness-header-2")));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.findElement(By.id("ui-accordion-lineOfBusiness-header-2")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchSic")));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.findElement(By.id("searchSic")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lnkPasteSicCodes")));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.findElement(By.id("lnkPasteSicCodes")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pastedSicCodes")));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.findElement(By.id("pastedSicCodes")).sendKeys(code);
		
		dr.findElement(By.id("submitCriteria")).click();
		try {
			waitTillTableLoad();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.findElement(By.id("jump-to-record-number")).sendKeys(page);
		dr.findElement(By.id("jump-to-record-number")).sendKeys(Keys.ENTER);
		try {
			waitTillTableLoad();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	protected void searchAll() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.findElement(By.id("buildListUsBusiness")).click();
		
		WebDriverWait wait = new WebDriverWait(dr, WAIT_LONG);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("critOfficeSize")));
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.findElement(By.id("critOfficeSize")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("squareFoot")));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<WebElement> result_list = dr.findElements(By.name("squareFoot"));
		if(result_list.isEmpty()) {
			System.out.println("No records matching.");
		}else {
			for(WebElement element : result_list) {
				element.click();
			}
			dr.findElement(By.id("submitCriteria")).click();
		}
		try {
			waitTillTableLoad();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	protected boolean searchKeywords(String keywords, String location) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.findElement(By.id("buildListUsBusiness")).click();
		WebDriverWait wait = new WebDriverWait(dr, WAIT_LONG);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("critLineOfBusinessSic")));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.findElement(By.id("critLineOfBusinessSic")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sicLookupKeyword")));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.findElement(By.id("sicLookupKeyword")).sendKeys(keywords);
		dr.findElement(By.className("action-sic-lookup")).click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement results = dr.findElement(By.id("sicLookupResults"));
		List<WebElement> result_list = results.findElements(By.tagName("li"));
		if(result_list.isEmpty()) {
			System.out.println("No records matching.");
			return false;
		}else {
			for(WebElement element : result_list) {
				element.click();
			}
			dr.findElement(By.id("submitCriteria")).click();
		}
			
		try {
			waitTillTableLoad();
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void waitTillTableLoad() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(dr, 60);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("grid-view-table-container")));
		Thread.sleep(5000);
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
		System.out.println(newText);
		Thread.sleep(3000); // Salesgenie will load for a longer time without this line
	}
	
	protected ArrayList<SalesGenieResult> crawlSalesgenieResults() {
		ArrayList<SalesGenieResult> resultList = new ArrayList<SalesGenieResult>();
		resultList.addAll(crawlCurrentTable());
		WebElement pageNextBtn = dr.findElement(By.xpath("//div[contains(@class, 'action-page-next')]"));
		if (pageNextBtn.getAttribute("aria-disabled").equals("false")) {
			pageNextBtn.click();
			try {
				waitTillTableRefresh(WAIT_LONG);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("no more result avaliable");
		}
	
		return resultList;
	}
	
	protected void crawSalesgenieHTML() throws InterruptedException {
		try {
			waitTillTableLoad();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement companyLink = dr.findElement(By.className("detailLink"));
		companyLink.click();
		WebDriverWait wait = new WebDriverWait(dr, WAIT_LONG);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("HistoricalData")));
		while(true) {
			Thread.sleep(5000);
			String companyName = dr.findElement(By.className("data-business-name")).getText();
			String pageHTML = dr.getPageSource();
			CompanyDAO.updateHTML(companyName, pageHTML);
			WebElement nextButton = dr.findElement(By.className("next"));
			nextButton.click();
		}
		
	}
	
	protected ArrayList<SalesGenieResult> crawlAllSalesgenieResults(){
		ArrayList<SalesGenieResult> resultList = new ArrayList<SalesGenieResult>();
		resultList.addAll(crawlCurrentTable());
		WebElement pageNextBtn = dr.findElement(By.xpath("//div[contains(@class, 'action-page-next')]"));
		if (pageNextBtn.getAttribute("aria-disabled").equals("false")) {
			pageNextBtn.click();
			try {
				waitTillTableRefresh(WAIT_LONG);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("no more result avaliable");
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
				//System.out.print(companyName + " ");
				result.setCompanyName(companyName);
				//System.out.print(personName + " ");
				result.setPersonName(personName);
				//System.out.print(phoneNumber + " ");
				result.setPhoneNumber(phoneNumber);
				//System.out.println(title + " ");
				result.setTitle(title);
				//System.out.print(street + " ");
				result.setStreet(street);
				//System.out.print(city + " ");
				result.setCity(city);
				//System.out.print(state + " ");
				result.setState(state);
				//System.out.println(zipCode + " ");
				result.setZipCode(zipCode);
				return result;
			}
		}
		return result;
	}
	public boolean haveNext() {
		WebElement pageNextBtn = dr.findElement(By.xpath("//div[contains(@class, 'action-page-next')]"));
		if (pageNextBtn.getAttribute("aria-disabled").equals("false")) {
			return true;
		}
		return false;
	}
}
