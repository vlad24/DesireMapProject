package userDataPackage;

public final class MainData{
	private final String login;
	private final String name;
	private final char sex;
	private final String birth;
	private final int rating;
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
