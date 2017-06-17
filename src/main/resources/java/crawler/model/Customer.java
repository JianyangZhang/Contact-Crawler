package crawler.model;

public class Customer {
	String customer_linkedin_url;
	String customer_name;
	String customer_title;
	String customer_location;
	String customer_keywords;
	String customer_create_time;
	String customer_touch_time;
	String internal_company_id;
	String compaign_step;
	public String getCustomer_linkedin_url() {
		return customer_linkedin_url;
	}
	public void setCustomer_linkedin_url(String customer_linkedin_url) {
		this.customer_linkedin_url = customer_linkedin_url;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getCustomer_title() {
		return customer_title;
	}
	public void setCustomer_title(String customer_title) {
		this.customer_title = customer_title;
	}
	public String getCustomer_location() {
		return customer_location;
	}
	public void setCustomer_location(String customer_location) {
		this.customer_location = customer_location;
	}
	public String getCustomer_keywords() {
		return customer_keywords;
	}
	public void setCustomer_keywords(String customer_keywords) {
		this.customer_keywords = customer_keywords;
	}
	public String getCustomer_create_time() {
		return customer_create_time;
	}
	public void setCustomer_create_time(String customer_create_time) {
		this.customer_create_time = customer_create_time;
	}
	public String getCustomer_touch_time() {
		return customer_touch_time;
	}
	public void setCustomer_touch_time(String customer_touch_time) {
		this.customer_touch_time = customer_touch_time;
	}
	public String getInternal_company_id() {
		return internal_company_id;
	}
	public void setInternal_company_id(String internal_company_id) {
		this.internal_company_id = internal_company_id;
	}
	public String getCompaign_step() {
		return compaign_step;
	}
	public void setCompaign_step(String compaign_step) {
		this.compaign_step = compaign_step;
	}
	@Override
	public String toString() {
		return "Customer [customer_linkedin_url=" + customer_linkedin_url + ", customer_name=" + customer_name
				+ ", customer_title=" + customer_title + ", customer_location=" + customer_location
				+ ", customer_keywords=" + customer_keywords + ", customer_create_time=" + customer_create_time
				+ ", customer_touch_time=" + customer_touch_time + ", internal_company_id=" + internal_company_id
				+ ", compaign_step=" + compaign_step + "]";
	}
}