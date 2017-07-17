package crawler.service;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import crawler.EmailCrawlerConfig;
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
			ArrayList<SalesGenieResult> resultList = br.crawlSalesgenieResults(query.getCount());
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
}
