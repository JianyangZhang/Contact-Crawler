package crawler.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import crawler.EmailCrawlerConfig;
import crawler.model.Customer;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DriveLinkedinService extends DriveBrowserService {
	private String baseURL;
	private int pagesAccess = 0;
	private int pageLimit = 800;

	DriveLinkedinService() {
		super(Boolean.parseBoolean(EmailCrawlerConfig.getConfig().readString("show-gui")));
		pageLimit = Integer.parseInt(EmailCrawlerConfig.getConfig().readString("page-limit"));
		this.dr.get("http://www.linkedin.com");
	}
	
	private void pageIncease() throws InterruptedException {
		++pagesAccess;
		if(pagesAccess >= pageLimit) {
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("k"); // hour 1-24
			String time = formatter.format(date);
			int hour = Integer.parseInt(time);
			if(hour >= 17)
				Thread.sleep((41 - hour)*3600*1000);
			else
				Thread.sleep((17 - hour )*3600*1000);
				//Thread.sleep(10000);
			pagesAccess = 0;
		}
	}

	/**
	 * Linkedin page turn
	 * 
	 * @throws InterruptedException
	 */
	private void pageTurn(int page) throws InterruptedException {
		String targetURL = baseURL + "&page=" + page;
		dr.get(targetURL);
		int cur_height = screen_height;
		while(cur_height <= 1500) {
			scrollTo(dr, String.valueOf(cur_height));
			cur_height += screen_height;
		}
		pageIncease();
	}

	/**
	 * login Hunter
	 */
	protected void signInHunter(String userName, String password) {
		dr.get("https://hunter.io/users/sign_in");
		dr.findElement(By.id("email-field")).sendKeys(userName);
		dr.findElement(By.id("password-field")).sendKeys(password);
		WebElement loginButton = dr.findElement(By.xpath("//button[contains(@class, 'orange-btn')]"));
		loginButton.click();
	}

	/**
	 * login Linkedin
	 */
	protected void signInLinkedin(String userName, String password) {
		dr.findElement(By.name("session_key")).sendKeys(userName);
		dr.findElement(By.name("session_password")).sendKeys(password);
		dr.findElement(By.id("login-submit")).click();
	}

	/**
	 * search keyword in Linkedin
	 * 
	 * @throws InterruptedException
	 * @throws IOException 
	 **/
	protected void searchKeyword(String title) throws InterruptedException {
		String url = "https://www.linkedin.com/search/results/people/?keywords=" + title
				+ "&origin=GLOBAL_SEARCH_HEADER";
		baseURL = url;
		dr.get(url);
		int cur_height = screen_height;
		while(cur_height <= 1500) {
			scrollTo(dr, String.valueOf(cur_height));
			cur_height += screen_height;
		}
	}
	/*protected void searchKeyword(String title) throws InterruptedException, IOException {
		String url = "https://www.google.com/search?q=" + title.replace(" ", "+")
				+ "+site%3Awww.linkedin.com%2Fin%2F";
		Elements links = Jsoup.connect(url)
				.timeout(10000)
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0").get()
				.select(".g>.s>cite");
		if(links.isEmpty())
			return;
	}*/

	/**
	 * get people's basic info
	 */
	protected Customer getPeopleInfo(String title){
		Customer customer = new Customer();
		WebElement element = dr.findElement(By.id("name"));
		((JavascriptExecutor) dr).executeScript("arguments[0].scrollIntoView(true);", element);
		customer.setCustomer_name(element.getText());
		customer.setCustomer_title(title);
		customer.setCustomer_linkedin_url(dr.getCurrentUrl());
		return customer;
	}
	protected ArrayList<Customer> getPeopleInfo(int count) throws IOException, InterruptedException {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		int currentPage = 1;
		while (currentPage <= 100) {
			String page = dr.getPageSource();
			Document doc = Jsoup.parse(page);
			Elements elements_name = doc.select("span.name.actor-name");
			Elements elements_title = doc.select("div.search-results__primary-cluster p.subline-level-1");
			Elements elements_url = doc.select("a.search-result__result-link.ember-view");
			this.uniquifyUrl(elements_url);

			Iterator<Element> iter_name = elements_name.iterator();
			Iterator<Element> iter_title = elements_title.iterator();
			Iterator<Element> iter_url = elements_url.iterator();
			boolean hasURL = false;
			if (iter_name.hasNext()) {
				hasURL = true;
			}

			while (iter_name.hasNext() && iter_title.hasNext() && iter_url.hasNext()) {
				String name = ((Element) iter_name.next()).text();
				String title = ((Element) iter_title.next()).text();
				String url = "https://www.linkedin.com" + ((Element) iter_url.next()).attr("href");

				Customer customer = new Customer();
				customer.setCustomer_name(name);
				customer.setCustomer_title(title);
				customer.setCustomer_linkedin_url(url);
				customers.add(customer);
				//System.out.println("name: " + name + " | " + "title: " + title + " | " + "url: " + url);
			}
			//System.out.println("current url Set size: " + customers.size());
			if (customers.size() < count && hasURL) {
				pageTurn(++currentPage);
			} else {
				break;
			}
		}
		return customers;
	}

	/**
	 * extract company name/college name
	 */
	protected HashSet<String> extractInstitution() throws InterruptedException {
		pageIncease();
		HashSet<String> result = new HashSet<String>();
		int cur_height = screen_height;
		while(cur_height <= 2000) {
			scrollTo(dr, String.valueOf(cur_height));
			cur_height += screen_height;
		}
		List<WebElement> webElements = dr.findElements(By
				.xpath("//section[contains(@class, 'experience-section')]//span[@class='pv-entity__secondary-title']"));
		// System.out.println(webElements.isEmpty());
		for (WebElement e : webElements) {
			result.add(e.getText().toLowerCase());
		}
		return result;
	}

	protected ArrayList<ArrayList<String>> extractInstitution_google() throws InterruptedException {
		pageIncease();
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>(4);
		for(int i=0; i<4; ++i)
			result.add(new ArrayList<String>());
		int cur_height = screen_height;
		while(cur_height <= 2500) {
			scrollTo(dr, String.valueOf(cur_height));
			cur_height += screen_height;
		}
		List<WebElement> webElements1 = dr.findElements(By
				.xpath("//ul[@class='positions']/li/header/h4[@class='item-title']"));//
		List<WebElement> webElements2 = dr.findElements(By
				.xpath("//ul[@class='positions']/li/header/h5[@class='item-subtitle']"));
		List<WebElement> webElements3 = dr.findElements(By
				.xpath("//ul[@class='schools']/li/header/h4[@class='item-title']"));//
		List<WebElement> webElements4 = dr.findElements(By
				.xpath("//ul[@class='schools']/li/header/h5[@class='item-subtitle']/span[@class='original translation']"));
		
		// System.out.println(webElements.isEmpty());
		for (int i=0; i< webElements1.size(); ++i) {
			String title;
			List<WebElement> link_tag = webElements1.get(i).findElements(By.tagName("a"));
			((JavascriptExecutor) dr).executeScript("arguments[0].scrollIntoView(true);", webElements1.get(i));
			Thread.sleep(500);
			if(link_tag.size()>0)
				title = link_tag.get(0).getText();
			else
				title = webElements1.get(i).getText();
			String company;
			
			link_tag = webElements2.get(i).findElements(By.tagName("a"));
			if(link_tag.size()>0)
				company = link_tag.get(0).getText();
			else{
				company = webElements2.get(i).getText();
			}
			result.get(0).add(title.toLowerCase());
			result.get(1).add(company.toLowerCase());
		}
		for (int i=0; i< webElements3.size(); ++i) {
			String school;
			List<WebElement> link_tag = webElements3.get(i).findElements(By.tagName("a"));
			((JavascriptExecutor) dr).executeScript("arguments[0].scrollIntoView(true);", webElements3.get(i));
			if(link_tag.size() > 0)
				school = link_tag.get(0).getText();
			else {
				school = webElements3.get(i).getText();
			}
			String level;
			level = webElements4.get(i).getText();
			result.get(2).add(school.toLowerCase());
			result.get(3).add(level.toLowerCase());
		}
		
		return result;
	}

	/**
	 * uniquify url (de-duplication)
	 */
	private void uniquifyUrl(Elements elements) {
		String prev;
		Iterator<Element> iter = elements.iterator();
		if (iter.hasNext()) {
			prev = ((Element) iter.next()).attr("href");
		} else {
			return;
		}
		while (iter.hasNext()) {
			String cur = ((Element) iter.next()).attr("href");
			if (cur.equals(prev)) {
				iter.remove();
			} else {
				prev = cur;
			}
		}
	}
}