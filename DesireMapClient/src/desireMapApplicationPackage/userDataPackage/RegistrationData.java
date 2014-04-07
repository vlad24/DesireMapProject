package desireMapApplicationPackage.userDataPackage;

import java.io.Serializable;

public final class RegistrationData implements Serializable{
	
	public final String login;
	public final String password;
	public final String name;
	public final char sex;
	public final String birth;
	// private String references;
	// private final Picture photo
	
	public RegistrationData(String newLogin, String newPassword, String newName, char newSex, String newBirth){
		login = newLogin;
		password = newPassword;
		name = newName;
		sex = newSex;
		birth = newBirth;
	}
	
}
