//TO DO RSA encryption
package queryObjectPackage.userDataPackage.userDataTypesPackage;

import queryObjectPackage.userDataPackage.UserData;

public final class LoginData extends UserData{
	
	private final String password;
	////
	LoginData(char nActionCode, String nLogin, int nUserId, String newPassword){
		super(nActionCode, 'L', nLogin, nUserId);
		password = newPassword;		
	}
}
