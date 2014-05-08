package desireMapApplicationPackage.desireContentPackage;

import java.util.Date;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;

public final class DesireContentSport extends DesireContent{
	public final String sportName;
	public final String advantages;
	////
	public DesireContentSport(String nLogin,String nDesireID, String nDesireDescription, Coordinates nCoordinates, Date nTime, int nLikes,
			String newSportName, String newAdvantages){
		super(nLogin , nDesireID, nDesireDescription, nCoordinates,  CodesMaster.Categories.SportCode, nTime, nLikes);
		sportName = newSportName;
		advantages = newAdvantages;
	}
	
}
