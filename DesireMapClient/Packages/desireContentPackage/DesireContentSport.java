package desireContentPackage;

import java.io.Serializable;

import actionCodeConstantsPackage.ActionCodesMaster;



public final class DesireContentSport extends DesireContent{
	private final String sportName;
	private final int advantages;
	//and so on
	////
	public DesireContentSport(String nLogin, int nDesireID, String nDesireDescription, Coordinates nCoordinates,
			String newSportName, int newAdvantages){
		super(nLogin , nDesireID, nDesireDescription, nCoordinates,  ActionCodesMaster.Categories.Sport);
		sportName = newSportName;
		advantages = newAdvantages;
	}
	
	public String getSportName() {
		return sportName;
	}
	public int getAdvantages() {
		return advantages;
	}
	
}
