package crawler.service;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DriveSalesgenieService extends DriveBrowserService{

	DriveSalesgenieService(boolean hasGUI) {
		super(hasGUI);
		this.dr.get("https://www.salesgenie.com/");
	}
	
	/**
	 * Login Salesgenie
	 */
	protected void signInSalesgenie(String userName, String password) {
		WebElement signInButton = dr.findElement(By.xpath("//li[contains(@class, 'pre-auth')]"));
		signInButton.click();
		dr.findElement(By.id("UserName")).sendKeys(userName);
		dr.findElement(By.id("Password")).sendKeys(password);
		dr.findElement(By.id("formSignIn")).click();
		try {
			dr.findElement(By.id("submit")).click();
			dr.switchTo().alert().accept();
		} catch (Exception e) {
		}
	}
}
