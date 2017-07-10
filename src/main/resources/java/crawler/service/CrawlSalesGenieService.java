package crawler.service;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import crawler.DAO.SalesgenieDAO;
import crawler.model.SalesGenieResult;

public class CrawlSalesGenieService {
	protected static DriveSalesgenieService br;
	public static void crawl(String keywords, int count) {
		br = new DriveSalesgenieService();
		br.signInSalesgenie("buzzrehnberg@gmail.com", "5056Veloz321");
		br.searchKeywords(keywords);
		ArrayList<SalesGenieResult> resultList = br.crawlSalesgenieResults(count);
		for (SalesGenieResult result : resultList) {
			SalesgenieDAO.insert(result.getPersonName(), result.getPhoneNumber(), result.getCompanyName(), result.getStreet(), result.getCity(), result.getState(), result.getZipCode());
		}
		br.dr.close();
	}
}
