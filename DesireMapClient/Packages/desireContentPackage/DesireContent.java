package desireContentPackage;


public abstract class DesireContent{
	
	protected String login;
	protected int desireID;
	protected final String description;
	protected final Coordinates coordinates;
	protected final String category;
	////
	protected DesireContent(String newLogin, int newID, String newDescription, Coordinates newCoordinates, String newCategory){
		login = newLogin;
		desireID = newID;
		description = newDescription;
		coordinates = newCoordinates;
		category = newCategory;
	}
	
	public String getLogin(){
		return login;
	}
	
	public int getDesireID(){
		return desireID;
	}
	
	public String getDesireDescription(){
		return description;
	}
	
	public String getCategory(){
		return category;
	}
	
}
