package desireMapApplicationPackage.userDataPackage;

import java.io.Serializable;

public final class MainData implements Serializable{
	public final String login;
	public final String name;
	public final char sex;
	public final String birth;
	public final int rating;
	//public String references;
	//public Picture photo;
	////
	public MainData(String newLogin, String newName, char newSex, String newBirth, int newRating){
		login = newLogin;
		name = newName;
		sex = newSex;
		birth = newBirth;
		rating = newRating;
	}
	
}
