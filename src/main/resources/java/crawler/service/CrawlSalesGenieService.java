package crawler.service;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class CrawlSalesGenieService {
	protected static DriveSalesgenieService br;
	public static void main(String[] args) {
		br = new DriveSalesgenieService(true);
		br.signInSalesgenie("buzzrehnberg@gmail.com", "5056Veloz321");
		List<WebElement> webElements = br.dr.findElements(By.xpath("//h3[contains(@class, 'ssg')]//a[contains(@class, 'action-restore-search')]"));
		for (WebElement element : webElements) {
			element.click();
			br.crawlCurrentTable();
		}
	}
}
