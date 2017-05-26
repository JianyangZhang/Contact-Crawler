import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class UnitOne {
	

	public static void main(String[] args) {
		
		System.setProperty("webdriver.chrome.driver", "/Applications/chromedriver");
		WebDriver dr = new ChromeDriver();
		dr.get("http://www.linkedin.com");
		signInLinkedin("velozproject@gmail.com","19940916",dr);
		
		
		
		
		
//		 try {
//	            Thread.sleep(3000);
//	        } catch (InterruptedException e) {
//	            // TODO Auto-generated catch block
//	            e.printStackTrace();
//	        }
//	     
//	        dr.quit();
//		
		

	}
	
	/**
	 * Automatically sign in Linkedin 
	 */
	public static void signInLinkedin(String userName, String password, WebDriver dr){
		dr.findElement(By.name("session_key")).sendKeys(userName);
		dr.findElement(By.name("session_password")).sendKeys(password);
		dr.findElement(By.id("login-submit")).click();
	}

}
