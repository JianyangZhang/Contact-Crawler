package crawler.model;
import java.util.ArrayList;
import java.util.HashMap;

import crawler.service.EmailVerifier;

public class Customer {
	private String name;
	private HashMap<String, String> domainMap;
	private EmailVerifier ev = new EmailVerifier();
	
	public Customer(String name, HashMap<String, String> domainMap) {
		this.name = name;
		this.domainMap = domainMap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, String> getDomainMap() {
		return domainMap;
	}

	public void setDomainMap(HashMap<String, String> domainMap) {
		this.domainMap = domainMap;
	}

	@Override
	public String toString() {
		String result = name + " | ";
		for (String key : domainMap.keySet()) {
			result = result + key + " - " + domainMap.get(key) + " | "; 
		}
		return result;
	}
	
	public ArrayList<String> getEmails() {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> usernames = guessPrefix(name);
		for (String domain : domainMap.values()) {
			for (String username : usernames) {
				String email = username + "@" + domain;
				if (ev.valid(email, "outlook.com")) {
					result.add(email);
					System.out.println(email);
				}
			}
		}
		return result;
	}
	
	/**
	 * guess email prefix
	 */
	private static ArrayList<String> guessPrefix(String name) {
		name = name.toLowerCase();
		String firstName = name.substring(0, name.indexOf(" "));
		String lastName = name.substring(name.indexOf(" ") + 1, name.length());
		char lastName_initial = lastName.charAt(0);
		ArrayList<String> result = new ArrayList<String>();
		result.add(firstName);
		result.add(firstName + lastName);
		result.add(firstName + lastName_initial);
		result.add(lastName_initial + firstName);
		result.add(firstName + "." + lastName);
		result.add(lastName + "." + firstName);
		result.add(firstName + "_" + lastName);
		result.add(lastName + "_" + firstName);
		result.add(firstName + "-" + lastName);
		result.add(lastName + "-" + firstName);
		return result;
	}
}
