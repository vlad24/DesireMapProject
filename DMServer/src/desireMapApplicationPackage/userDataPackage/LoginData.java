package desireMapApplicationPackage.userDataPackage;

import java.io.Serializable;

public final class LoginData implements Serializable{
	public final String login;
	public final String password;
	public LoginData(String newLogin, String newPassword){
		login = newLogin;
		password = newPassword;		
	}
}