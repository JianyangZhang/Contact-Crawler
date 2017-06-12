package crawler.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import crawler.model.CrawlerQuery;
import crawler.model.Customer;

@Service
public class CrawlEmailService {
	protected static BrowserDriver br;
/*
	public static void main(String[] args) {
		crawl("uber", 10);
	}
*/

	public static ArrayList<Customer> crawl(CrawlerQuery query) {
		ArrayList<Customer> result = new ArrayList<Customer>();
		BrowserDriver br = new BrowserDriver();
		br.signInLinkedin("wangwent@usc.edu", "19940916".toCharArray());
		try {
			br.searchKeyword(query.getKeyword());
			HashSet<String> urls = br.getPeopleUrl(query.getCount());
			int flag = 0;
			for (String url : urls) {
				BrowserDriver.dr.get(url);
				String name = br.extractName();
				HashSet<String> institutionSet = br.extractInstitution();
				HashMap<String, String> domainMap = br.parseDomainFromGoogle(institutionSet);
				// WebElement hunterButton =
				// dr.findElement(By.xpath("//button[contains(@class,
				// 'ehunter_linkedin_button')]"));
				// hunterButton.click();
				Customer customer = new Customer(name, domainMap);
				System.out.println(name + "'s verified emails:");
				customer.getEmails();
				System.out.println("------------------------------------------");
				result.add(customer);
				flag++;
				if (flag == query.getCount()) {
					break;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return result;
	}
}
