package desireMapApplicationPackage.desireContentPackage;

import java.io.Serializable;

public class DesireContent implements Serializable{
	
	public final String login;
	public final int desireID;
	public final String description;
	public final Coordinates coordinates;
	public final int category;
	////
	public DesireContent(String newLogin, int newID, String newDescription, Coordinates newCoordinates, int newCategory){
		login = newLogin;
		desireID = newID;
		description = newDescription;
		coordinates = newCoordinates;
		category = newCategory;
	}
	
}
