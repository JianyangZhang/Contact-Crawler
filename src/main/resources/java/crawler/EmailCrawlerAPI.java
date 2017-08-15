package crawler;
import java.io.IOException;
import java.util.Timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import crawler.DAO.RecnctThread;
import crawler.service.PollSearchQueryService;
import crawler.service.CrawlSalesGenieService;


@SpringBootApplication
public class EmailCrawlerAPI {
	public static void main(String[] args) {
		SpringApplication.run(EmailCrawlerAPI.class, args);
		new Timer().schedule(new PollSearchQueryService(), 0, EmailCrawlerConfig.getConfig().readInt("scan-interval"));
		new Timer().schedule(new RecnctThread(),  0, 1400000);
		//CrawlSalesGenieService.crawlAll();
		//CrawlSalesGenieService.crawlEmail();
		/*try {
			if(args.length == 0)
				CrawlSalesGenieService.crawlSIC("");
			else
				CrawlSalesGenieService.crawlSIC(args[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}
}