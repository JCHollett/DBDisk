package User;

public class User {
	private String name;
	private String pwd;
	
	public User(String name, String pwd) {
		this.name = name;
		this.pwd = pwd;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPassword() {
		return this.pwd;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPassword(String pwd) {
		this.pwd = pwd;
	}
}
