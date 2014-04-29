package desireMapApplicationPackage.desireContentPackage;

import java.io.Serializable;
import java.util.Date;

public class DesireContent implements Serializable{
	
	public final String login;
	public String desireID;
	public final String description;
	public final Coordinates coordinates;
	public final int category;
	public final Date time;
	////
	public DesireContent(String newLogin, String newID, String newDescription, Coordinates newCoordinates, int newCategory, Date newTime){
		login = newLogin;
		desireID = newID;
		description = newDescription;
		coordinates = newCoordinates;
		category = newCategory;
		time = newTime;
	}
	
}
