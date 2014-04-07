package userDataPackage;

public final class RegistrationData{
	
	private final String login;
	private final String password;
	private final String name;
	private final char sex;
	private final String birth;
	// private String references;
	// private final Picture photo
	
	public RegistrationData(String newLogin, String newPassword, String newName, char newSex, String newBirth){
		login = newLogin;
		password = newPassword;
		name = newName;
		sex = newSex;
		birth = newBirth;
		
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public char getSex() {
		return sex;
	}

	public String getBirth() {
		return birth;
	}
	
}
