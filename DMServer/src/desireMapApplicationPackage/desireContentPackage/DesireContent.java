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
	public int likes;
	////
	public DesireContent(String newLogin, String newID, String newDescription, Coordinates newCoordinates, int newCategory, Date newTime, int newLikes){
		login = newLogin;
		desireID = newID;
		description = newDescription;
		coordinates = newCoordinates;
		category = newCategory;
		time = newTime;
		likes = newLikes;
	}
	
}
