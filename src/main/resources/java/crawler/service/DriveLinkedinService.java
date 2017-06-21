package crawler.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import crawler.DAO.ResultDAO;
import crawler.model.Customer;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DriveLinkedinService extends DriveBrowserService {
	DriveLinkedinService() {
		super(false);
		this.dr.get("http://www.linkedin.com");
	}
	
	/**
	 * Linkedin page turn
	 * @throws InterruptedException 
	 */
	private void pageTurn(int page) throws InterruptedException {
		String targetURL = dr.getCurrentUrl() + "&page=" + page;
		dr.get(targetURL);
		scrollTo(dr, "500");
		scrollTo(dr, "1000");
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
	protected void signInLinkedin(String userName, char[] password) {
		dr.findElement(By.name("session_key")).sendKeys(userName);
		dr.findElement(By.name("session_password")).sendKeys(new String(password));
		dr.findElement(By.id("login-submit")).click();
	}

	/**
	 * search keyword in Linkedin
	 * @throws InterruptedException
	 **/
	protected void searchKeyword(String title) throws InterruptedException {
		String url = "https://www.linkedin.com/search/results/people/?keywords=" + title
				+ "&origin=GLOBAL_SEARCH_HEADER";
		dr.get(url);
		scrollTo(dr, "500");
		scrollTo(dr, "1000");
	}

	/**
	 * get people's basic info
	 */
	protected ArrayList<Customer> getPeopleInfo(int count) throws IOException, InterruptedException {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		int currentPage = 1;
		while (true) {
			String page = dr.getPageSource();
			Document doc = Jsoup.parse(page);
			Elements elements_name = doc.select("span.name.actor-name");
			Elements elements_title = doc.select("div.search-results__primary-cluster p.subline-level-1");
			Elements elements_url = doc.select("a.search-result__result-link.ember-view");
			this.uniquifyUrl(elements_url);
			
			Iterator iter_name = elements_name.iterator();
			Iterator iter_title = elements_title.iterator();
			Iterator iter_url = elements_url.iterator();
			
			while (iter_name.hasNext() && iter_title.hasNext() && iter_url.hasNext()) {
				String name = ((Element) iter_name.next()).text();
				String title = ((Element) iter_title.next()).text();
				String url = "https://www.linkedin.com" + ((Element) iter_url.next()).attr("href");

				Customer customer = new Customer();
				customer.setCustomer_name(name);
				customer.setCustomer_title(title);
				customer.setCustomer_linkedin_url(url);
				customers.add(customer);
				System.out.println("name: " + name + " | " + "title: " + title + " | " + "url: " + url);
			}
			System.out.println("current url Set size: " + customers.size());
			if (customers.size() < count) {
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
		HashSet<String> result = new HashSet<String>();
		scrollTo(dr, "500");
		scrollTo(dr, "1000");
		scrollTo(dr, "1500");
		scrollTo(dr, "2000");
		List<WebElement> webElements = dr.findElements(By
				.xpath("//section[contains(@class, 'experience-section')]//span[@class='pv-entity__secondary-title']"));
		// System.out.println(webElements.isEmpty());
		for (WebElement e : webElements) {
			result.add(e.getText().toLowerCase());
		}
		return result;
	}

	/**
	 * find email domain from google search
	 * @throws IOException
	 */
	protected HashMap<String, String> parseDomainFromGoogle(HashSet<String> institutionSet) throws IOException {
		HashMap<String, String> result = new HashMap<String, String>();
		int flag = 0;
		for (String s : institutionSet) {
			String query = "https://www.google.com/search?q=" + s.replace(" ", "+") + "+official" + "+site";
//			System.out.println("query: " + query);
			Elements links = Jsoup.connect(query)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0").get()
					.select(".g>.r>a");
			String title = links.first().text();
			String url = links.first().absUrl("href"); // Google returns URLs in
														// format
														// "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
			url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");
			String domain;
			try {
				domain = domainFromURL(url);
//				System.out.println("Official Site Title: " + title);
//				System.out.println("Official Site URL: " + url);
//				System.out.println("Domain: " + domain);
				result.put(s, domain);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * uniquify url (de-duplication)
	 */
	private void uniquifyUrl(Elements elements) {
		String prev;
		Iterator iter = elements.iterator();
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

	/**
	 * substring domain from url
	 */
	private String domainFromURL(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
}