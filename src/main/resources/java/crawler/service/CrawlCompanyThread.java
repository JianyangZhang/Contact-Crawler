package crawler.service;

import java.util.ArrayList;
import java.util.HashMap;

public class CrawlCompanyThread implements Runnable{
	private Thread t;
	private String threadName;
	private ArrayList<String> usernames;
	private String company;
	private String domain;
	private HashMap<String, String> results;
	private EmailVerifyService ev;
	
	CrawlCompanyThread(String name, ArrayList<String> usernames, String company, String domain, HashMap<String, String> results, EmailVerifyService ev){
		threadName = name;
		this.usernames = usernames;
		this.company = company;
		this.domain = domain;
		this.results = results;
		this.ev = ev;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (String username : usernames) {
			String email = username + "@" + domain;
			if (ev.valid(email, "gmail.com")) {
				results.put(email, company);
				System.out.println(email);
			} else {
				// System.out.println(email + "(invalid)");
			}
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
