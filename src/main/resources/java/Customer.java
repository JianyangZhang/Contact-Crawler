import java.util.HashMap;

public class Customer {
	String name;
	HashMap<String, String> domainMap;
	
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
}
