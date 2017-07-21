package crawler.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import crawler.EmailCrawlerConfig;
import crawler.DAO.CompanyDAO;
import crawler.DAO.ResultSgDAO;
import crawler.DAO.SalesgenieDAO;
import crawler.model.CrawlerQuery;
import crawler.model.SalesGenieResult;

public class CrawlSalesGenieService {
	protected static DriveSalesgenieService br;
	public static void crawl(Callback callback, CrawlerQuery query) {
		br = new DriveSalesgenieService();
		br.signInSalesgenie(EmailCrawlerConfig.readString("salesgenie-username"),
				EmailCrawlerConfig.readString("salesgenie-password"));
		if(br.searchKeywords(query.getKeyword(), query.getLocation())) {
			int count = 0;
			while (count < query.getCount()) {
				ArrayList<SalesGenieResult> resultList = br.crawlSalesgenieResults();
				if(resultList.isEmpty())
					break;
				count += resultList.size();
				for (SalesGenieResult result : resultList) {
					SalesgenieDAO.insert(result.getPersonName().replace("'", "''"),
							result.getPhoneNumber().replace("'", "''"),
							result.getTitle().replace("'", "''"),
							result.getCompanyName().replace("'", "''"),
							result.getStreet().replace("'", "''"),
							result.getCity().replace("'", "''"),
							result.getState(),
							result.getZipCode());
					ResultSgDAO.insert(query.getSearchID(),
							result.getPersonName().replace("'", "''"),
							result.getPhoneNumber().replace("'", "''"),
							result.getCompanyName().replace("'", "''"));
				}
			}
		}
		callback.process(PollSearchQueryService.COMPLETED);
		br.dr.close();
	}
	public static void crawlAll() {
		br = new DriveSalesgenieService();
		br.signInSalesgenie(EmailCrawlerConfig.readString("salesgenie-username"),
				EmailCrawlerConfig.readString("salesgenie-password"));
		br.searchAll();
		while(true) {
			ArrayList<SalesGenieResult> resultList = br.crawlAllSalesgenieResults();
			if(resultList.isEmpty())
				break;
			for (SalesGenieResult result : resultList) {
				SalesgenieDAO.insert(result.getPersonName().replace("'", "''"),
						result.getPhoneNumber().replace("'", "''"),
						result.getTitle().replace("'", "''"),
						result.getCompanyName().replace("'", "''"),
						result.getStreet().replace("'", "''"),
						result.getCity().replace("'", "''"),
						result.getState(),
						result.getZipCode());
				ResultSgDAO.insert("0",
						result.getPersonName().replace("'", "''"),
						result.getPhoneNumber().replace("'", "''"),
						result.getCompanyName().replace("'", "''"));
			}
		}
	}
	public static void crawlEmail() {
		br = new DriveSalesgenieService();
		ResultSet resultSet = SalesgenieDAO.getSGNamesAll();
		try {
			while(resultSet.next()) {
				String name = resultSet.getString(1);
				String inst = resultSet.getString(2);
				String title = resultSet.getString(3);
				HashSet<String> instSet = new HashSet<String>();
				instSet.add(inst);
				HashMap<String, String> domainMap = br.parseDomainFromGoogle(instSet);
				GenerateAccurateEmailsService gaes = new GenerateAccurateEmailsService(name.replace("'", ""), domainMap, false);
				//System.out.println("! " + customer.getCustomer_name() + "'s verified emails:");
				HashMap<String, String> emailsMap = gaes.getEmails();
				if(emailsMap.isEmpty()) {
					SalesgenieDAO.updateEmail(name, inst, title, "NULL");
				}
				for (String s : domainMap.keySet()) {
					CompanyDAO.insert(domainMap.get(s), s);
				}
				for (String email : emailsMap.keySet()) {
					SalesgenieDAO.updateEmail(name, inst, title, email);
				}
			}
			br.dr.quit();
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
