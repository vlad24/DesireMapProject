package userDataPackage;
//TO DO RSA encryption


import actionCodeConstantsPackage.ActionCodesMaster;

public final class LoginData{
	
	private final String login;
	private final String password;
	////
	LoginData(String newLogin, String newPassword){
		login = newLogin;
		password = newPassword;		
	}
	
	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}
}
