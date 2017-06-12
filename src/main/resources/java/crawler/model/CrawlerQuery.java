package crawler.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CrawlerQuery {
	@Id
	private String keyword;
	private int	count;
	
	public CrawlerQuery() {
		super();
	}
	
	public CrawlerQuery(String keyword, int count) {
		super();
		this.keyword = keyword;
		this.count = count;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}

