package queryObjectPackage.desirePackPackage.desireCategoriesPackage;

import java.io.Serializable;

import queryObjectPackage.desirePackPackage.DesirePack;

public final class DesirePackSport extends DesirePack{
	private final String sportName;
	private final String advantages;
	//and so on
	////
	public DesirePackSport(String nLogin, String nDesireDescription, double nLatitude, double nLongitude, String newSportName, String newAdvantages){
		super('s', nLogin, nDesireDescription, nLatitude, nLongitude);
		//
		sportName = newSportName;
		advantages = newAdvantages; 
		
	}
	
	public String getSportName() {
		return sportName;
	}
	public String getAdvantages() {
		return advantages;
	}
	
}
