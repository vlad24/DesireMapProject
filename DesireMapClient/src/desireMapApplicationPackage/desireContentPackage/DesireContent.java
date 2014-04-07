package desireMapApplicationPackage.desireContentPackage;

import java.io.Serializable;

import com.google.android.gms.maps.model.LatLng;


public class DesireContent implements Serializable{
	
	public final String login;
	public final int desireID;
	public final String description;
	public final LatLng coordinates;
	public final int category;
	////
	public DesireContent(String newLogin, int newID, String newDescription, LatLng newCoordinates, int newCategory){
		login = newLogin;
		desireID = newID;
		description = newDescription;
		coordinates = newCoordinates;
		category = newCategory;
	}
}
