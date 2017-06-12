package crawler.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import crawler.model.CrawlerQuery;
import crawler.service.CrawlEmailService;

@RestController
public class CrawlEmailController {
	
	@Autowired
	private CrawlEmailService crawlEmailService;
	
	@CrossOrigin
	@RequestMapping("/test")
	public void test() {
		CrawlerQuery query = new CrawlerQuery();
		query.setKeyword("uber");
		query.setCount(10);
		crawlEmailService.crawl(query);
	}
	
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, value="/crawl")
	public void crawl(@RequestBody CrawlerQuery query) {
		
	}
}