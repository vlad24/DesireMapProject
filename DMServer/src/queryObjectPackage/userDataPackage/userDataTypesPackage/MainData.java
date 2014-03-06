package queryObjectPackage.userDataPackage.userDataTypesPackage;

import java.io.Serializable;

import queryObjectPackage.userDataPackage.UserData;

public final class MainData extends UserData{
	private final String name;
	private final char sex;
	private final String birth;
	private final int rating;
	//public String references;
	//public Picture photo;
	////
	public MainData(String nLogin, int nUserId, String newName, char newSex, String newBirth, int newRating){
		super('M', nLogin, nUserId);
		name = newName;
		sex = newSex;
		birth = newBirth;
		rating = newRating;
	}
	
}
