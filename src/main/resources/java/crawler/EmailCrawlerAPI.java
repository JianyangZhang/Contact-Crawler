package crawler;
import java.util.Timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import crawler.DAO.RecnctThread;
import crawler.service.CrawlSalesGenieService;
import crawler.service.PollSearchQueryService;


@SpringBootApplication
public class EmailCrawlerAPI {
	public static void main(String[] args) {
		SpringApplication.run(EmailCrawlerAPI.class, args);
		//new Timer().schedule(new PollSearchQueryService(), 0, EmailCrawlerConfig.getConfig().readInt("scan-interval"));
		//new Timer().schedule(new RecnctThread(),  0, 1440000);
		CrawlSalesGenieService.crawl("game", 10000);

	}
}