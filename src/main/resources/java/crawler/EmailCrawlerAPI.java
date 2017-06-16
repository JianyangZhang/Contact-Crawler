package crawler;
import java.util.Timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import crawler.service.PollSearchQueryService;


@SpringBootApplication
public class EmailCrawlerAPI {
	public static void main(String[] args) {
		SpringApplication.run(EmailCrawlerAPI.class, args);
		new Timer().schedule(new PollSearchQueryService(), 0, 10000);
	}
}