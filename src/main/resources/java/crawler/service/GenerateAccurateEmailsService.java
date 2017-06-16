package crawler.service;
import java.util.ArrayList;
import java.util.HashMap;

public class GenerateAccurateEmailsService {
	private String name;
	private HashMap<String, String> domainMap;
	private EmailVerifyService ev = new EmailVerifyService();
	
	public GenerateAccurateEmailsService(String name, HashMap<String, String> domainMap) {
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
	
	public HashMap<String, String> getEmails() {
		HashMap<String, String> result = new HashMap<String, String>();
		ArrayList<String> usernames = guessPrefix(name);
		for (String company : domainMap.keySet()) {
			for (String username : usernames) {
				String email = username + "@" + domainMap.get(company);
				if (ev.valid(email, "gmail.com")) {
					result.put(email, company);
					System.out.println(email);
				} else {
					// System.out.println(email + "(invalid)");
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
