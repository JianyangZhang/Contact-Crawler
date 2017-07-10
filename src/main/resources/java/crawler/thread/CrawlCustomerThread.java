package crawler.thread;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import crawler.DAO.CustomerDAO;
import crawler.DAO.EmailDAO;
import crawler.DAO.ResultDAO;
import crawler.model.CrawlerQuery;
import crawler.model.Customer;
import crawler.service.Callback;
import crawler.service.GenerateAccurateEmailsService;
import crawler.service.PollSearchQueryService;

public class CrawlCustomerThread implements Runnable{
	private Thread t;
	private String threadName;
	private Customer customer;
	private Callback callback;
	private CrawlerQuery query;
	private String url;
	private HashMap<String, String> domainMap;
	
	public CrawlCustomerThread( String name, Customer customer, String url, HashMap<String, String> domainMap, Callback callback, CrawlerQuery query) {
	      threadName = name;
	      System.out.println("Creating " +  threadName );
	      this.customer = customer;
	      this.callback = callback;
	      this.query = query;
	      this.url = url;
	      this.domainMap = domainMap;
	   }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			
			GenerateAccurateEmailsService gaes = new GenerateAccurateEmailsService(customer.getCustomer_name(), domainMap, false);
			System.out.println("! " + customer.getCustomer_name() + "'s verified emails:");
			HashMap<String, String> emailsMap = gaes.getEmails();
			//System.out.println("------------------------------------------");
			CustomerDAO.insert(url, customer.getCustomer_name(), customer.getCustomer_title(), "", query.getKeyword(), new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()), "", query.getInternalCompanyID(), "step0");
			ResultDAO.insert(query.getSearchID(), url);
			System.out.println("Email set size: " + emailsMap.size());
			for (String email : emailsMap.keySet()) {
				EmailDAO.insert(email, url, emailsMap.get(email), 0);
			}
		}catch(Exception exception) {
			if (callback != null) { callback.process(PollSearchQueryService.FAILED); }
			exception.printStackTrace();
			return;
		}
	}
	
	public void start () {
		System.out.println("Starting " +  threadName );
		if (t == null) {
			t = new Thread (this, threadName);
			t.start ();
		}
	}

	public void join () throws InterruptedException {
		System.out.println("Joining " +  threadName );
		if (t != null) {
			t.join();
		}
	}
}
