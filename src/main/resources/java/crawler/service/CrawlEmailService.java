package crawler.service;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import crawler.EmailCrawlerConfig;
import crawler.DAO.CustomerDAO;
import crawler.DAO.EmailDAO;
import crawler.DAO.MySQLConnector;
import crawler.DAO.ResultDAO;
import crawler.DAO.SearchQueryDAO;
import crawler.model.CrawlerQuery;
import crawler.model.Customer;
import crawler.model.Email;

@Service
public class CrawlEmailService {
	protected static DriveLinkedinService br;
/*
	public static void main(String[] args) {
		crawl("uber", 10);
	}
*/

	public static void crawl(Callback callback, CrawlerQuery query) {
		DriveLinkedinService br = new DriveLinkedinService();
		br.signInLinkedin(EmailCrawlerConfig.getConfig().readString("linkedin-username"), EmailCrawlerConfig.getConfig().readString("linkedin-password").toCharArray());
		try {
			br.searchKeyword(query.getKeyword());
			ArrayList<Customer> customers = br.getPeopleInfo(query.getCount());
			int flag = 0;
			for (Customer customer : customers) {
				if (flag % 5 == 0) {
					ResultSet hasDeleted = SearchQueryDAO.hasDeleted(query.getSearchID());
					if(hasDeleted.next() && hasDeleted.getInt(1) == 1) {
						System.out.println("This query has been deleted from front-end");
						if (callback != null) { callback.process(PollSearchQueryService.COMPLETED); }
						break;
					}
				}
				String url = customer.getCustomer_linkedin_url();
				// avoid repeated crawling
				if (ResultDAO.getAllByUrl(url).next()) {
					System.out.println(customer.getCustomer_name() + " is detected that he/she has been crawled before, his/her info will not be printed here but it will be in the final report");
					ResultDAO.insert(query.getSearchID(), url);
					flag++;
					if (flag == query.getCount()) {
						br.dr.close();
						if (callback != null) { callback.process(PollSearchQueryService.COMPLETED); }
						break;
					}
					continue;
				}
				br.dr.get(url);
				HashSet<String> institutionSet = br.extractInstitution();
				HashMap<String, String> domainMap = br.parseDomainFromGoogle(institutionSet);
				// WebElement hunterButton =
				// dr.findElement(By.xpath("//button[contains(@class,
				// 'ehunter_linkedin_button')]"));
				// hunterButton.click();
				GenerateAccurateEmailsService gaes = new GenerateAccurateEmailsService(customer.getCustomer_name(), domainMap);
				System.out.println("!" + customer.getCustomer_name() + "'s verified emails:");
				HashMap<String, String> emailsMap = gaes.getEmails();
				System.out.println("------------------------------------------");
				if (!emailsMap.isEmpty()) {
					CustomerDAO.insert(url, customer.getCustomer_name(), customer.getCustomer_title(), "", query.getKeyword(), new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()), "", query.getInternalCompanyID(), "step0");
					ResultDAO.insert(query.getSearchID(), url);
					for (String email : emailsMap.keySet()) {
						EmailDAO.insert(email, url, emailsMap.get(email), 0);
					}
				}
				flag++;
				if (flag == query.getCount()) {
					br.dr.close();
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
