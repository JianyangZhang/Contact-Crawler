package crawler.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CrawlerQuery {
	@Id
	private String searchID;
	private String keyword;
	private int	count;
	private String location;
	private String linkedinID;
	private String internalCompanyID;
	
	
	public String getSearchID() {
		return searchID;
	}
	public void setSearchID(String searchID) {
		this.searchID = searchID;
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
	public String getLinkedinID() {
		return linkedinID;
	}
	public void setLinkedinID(String linkedinID) {
		this.linkedinID = linkedinID;
	}
	public String getInternalCompanyID() {
		return internalCompanyID;
	}
	public void setInternalCompanyID(String internalCompanyID) {
		this.internalCompanyID = internalCompanyID;
	}
	@Override
	public String toString() {
		return "CrawlerQuery [searchID=" + searchID + ", keyword=" + keyword + ", count=" + count + ", linkedinID="
				+ linkedinID + ", internalCompanyID=" + internalCompanyID + "]";
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}

