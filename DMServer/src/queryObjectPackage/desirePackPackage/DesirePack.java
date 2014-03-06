package queryObjectPackage.desirePackPackage;

import java.io.Serializable;

import queryObjectPackage.QueryObject;

public abstract class DesirePack extends QueryObject{
	protected String login; 
	protected String desireDescription;
	protected double latitude;
	protected double longitude;
	////
	protected DesirePack(char nTypeCode, String newLogin, String newDesireDescription, double newLatitude, double newLongitude){
		super(nTypeCode);
		login = newLogin;
		desireDescription = newDesireDescription;
		latitude = newLatitude;
		longitude = newLongitude;
	}	
}
