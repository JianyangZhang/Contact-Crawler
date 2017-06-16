package crawler.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import crawler.DAO.EmailDAO;
import crawler.model.CrawlerQuery;
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
			HashSet<String> urls = br.getPeopleUrl(query.getCount());
			int flag = 0;
			for (String url : urls) {
				DriveBrowserService.dr.get(url);
				String name = br.extractName();
				HashSet<String> institutionSet = br.extractInstitution();
				HashMap<String, String> domainMap = br.parseDomainFromGoogle(institutionSet);
				// WebElement hunterButton =
				// dr.findElement(By.xpath("//button[contains(@class,
				// 'ehunter_linkedin_button')]"));
				// hunterButton.click();
				GenerateAccurateEmailsService gaes = new GenerateAccurateEmailsService(name, domainMap);
				System.out.println(name + "'s verified emails:");
				ArrayList<String> emails = gaes.getEmails();
				System.out.println("------------------------------------------");
				for (String email : emails) {
					EmailDAO.insert(email, url, 0);
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
