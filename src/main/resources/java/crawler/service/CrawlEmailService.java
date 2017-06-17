package crawler.service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import crawler.DAO.CustomerDAO;
import crawler.DAO.EmailDAO;
import crawler.model.CrawlerQuery;
import crawler.model.Customer;
import crawler.model.Email;

@Service
public class CrawlEmailService {
	protected static DriveBrowserService br;
/*
	public static void main(String[] args) {
		crawl("uber", 10);
	}
*/

	public static void crawl(Callback callback, CrawlerQuery query) {
		DriveBrowserService br = new DriveBrowserService();
		br.signInLinkedin("wangwent@usc.edu", "19940916".toCharArray());
		try {
			br.searchKeyword(query.getKeyword());
			ArrayList<Customer> customers = br.getPeopleInfo(query.getCount());
			int flag = 0;
			for (Customer customer : customers) {
				DriveBrowserService.dr.get(customer.getCustomer_linkedin_url());
				HashSet<String> institutionSet = br.extractInstitution();
				HashMap<String, String> domainMap = br.parseDomainFromGoogle(institutionSet);
				// WebElement hunterButton =
				// dr.findElement(By.xpath("//button[contains(@class,
				// 'ehunter_linkedin_button')]"));
				// hunterButton.click();
				GenerateAccurateEmailsService gaes = new GenerateAccurateEmailsService(customer.getCustomer_name(), domainMap);
				System.out.println(customer.getCustomer_name() + "'s verified emails:");
				HashMap<String, String> emailsMap = gaes.getEmails();
				System.out.println("------------------------------------------");
				if (!emailsMap.isEmpty()) {
					CustomerDAO.insert(customer.getCustomer_linkedin_url(), customer.getCustomer_name(), customer.getCustomer_title(), "", query.getKeyword(), new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()), "", query.getInternalCompanyID(), "step0");
					for (String email : emailsMap.keySet()) {
						EmailDAO.insert(email, customer.getCustomer_linkedin_url(), emailsMap.get(email), 0);
					}
				}
				flag++;
				if (flag == query.getCount()) {
					DriveBrowserService.dr.close();
					if (callback != null) { callback.process(PollSearchQueryService.COMPLETED); }
					break;
				}
			} // what if urls is not enough
		} catch (Exception exception) {
			if (callback != null) { callback.process(PollSearchQueryService.FAILED); }
			exception.printStackTrace();
			return;
		}
	}
}
