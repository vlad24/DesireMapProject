package desireMapApplicationPackage.desireContentPackage;


import java.text.SimpleDateFormat;
import java.util.Date;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;

public final class DesireContentSport extends DesireContent{
	public final String sportName;
	public final String advantages;
	////
	public DesireContentSport(String nLogin,String nDesireID, String nDesireDescription, Coordinates nCoordinates, Date nTime,
			String newSportName, String newAdvantages){
		super(nLogin , nDesireID, nDesireDescription, nCoordinates,  CodesMaster.Categories.SportCode, nTime);
		sportName = newSportName;
		advantages = newAdvantages;
	}
	
}
