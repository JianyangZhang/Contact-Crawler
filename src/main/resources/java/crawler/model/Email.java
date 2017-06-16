package crawler.model;

public class Email {
	private String email_address;
	private String customer_linkedin_url;
	private int email_response_time;
	
	public String getEmail_address() {
		return email_address;
	}
	
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	
	public String getCustomer_linkedin_url() {
		return customer_linkedin_url;
	}
	
	public void setCustomer_linkedin_url(String customer_linkedin_url) {
		this.customer_linkedin_url = customer_linkedin_url;
	}
	
	public int getEmail_response_time() {
		return email_response_time;
	}
	
	public void setEmail_response_time(int email_response_time) {
		this.email_response_time = email_response_time;
	}
	
	@Override
	public String toString() {
		return "Email [email_address=" + email_address + ", customer_linkedin_url=" + customer_linkedin_url
				+ ", email_response_time=" + email_response_time + "]";
	}
}
