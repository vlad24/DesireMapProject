package queryObjectPackage.userDataPackage.userDataTypesPackage;

import queryObjectPackage.userDataPackage.UserData;

public final class RegistrationData extends UserData{
	
	private final String password;
	private final String name;
	private final char sex;
	private final String birth;
	// private String references;
	// private final Picture photo
	
	public RegistrationData(char nActionCode, String nLogin, int nUserId, String newPassword, String newName, char newSex, String newBirth){
		super(nActionCode, 'R', newBirth, nUserId);
		password = newPassword;
		name = newName;
		sex = newSex;
		birth = newBirth;
		
	}
	
}
