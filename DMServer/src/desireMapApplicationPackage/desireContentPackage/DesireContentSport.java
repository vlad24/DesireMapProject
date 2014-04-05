package desireMapApplicationPackage.desireContentPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;

public final class DesireContentSport extends DesireContent{
	public final String sportName;
	public final int advantages;
	////
	public DesireContentSport(String nLogin, int nDesireID, String nDesireDescription, Coordinates nCoordinates,
			String newSportName, int newAdvantages){
		super(nLogin , nDesireID, nDesireDescription, nCoordinates,  CodesMaster.Categories.SportCode);
		sportName = newSportName;
		advantages = newAdvantages;
	}
	
}
