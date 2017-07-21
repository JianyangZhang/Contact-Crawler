package crawler.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import crawler.EmailCrawlerConfig;
import crawler.model.Customer;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DriveLinkedinService extends DriveBrowserService {
	private String baseURL;

	DriveLinkedinService() {
		super(Boolean.parseBoolean(EmailCrawlerConfig.getConfig().readString("show-gui")));
		this.dr.get("http://www.linkedin.com");
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

	/**
	 * get people's basic info
	 */
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
			System.out.println("current url Set size: " + customers.size());
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

	
}