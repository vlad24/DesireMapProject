package desireMapApplicationPackage.desireContentPackage;


import java.util.Date;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;


public final class DesireContentDating extends DesireContent{
	public final char partnerSex;
	public final int partnerAgeFrom;
	public final int partnerAgeTo;
	////
	public DesireContentDating(String nLogin, String nDesireID, String nDesireDescription, Coordinates nCoordinates, Date nTime,
			char newPartnerSex, int newPartnerAgeFrom, int newPartnerAgeTo){
		super(nLogin , nDesireID, nDesireDescription, nCoordinates, CodesMaster.Categories.DatingCode, nTime);
		partnerSex = newPartnerSex;
		partnerAgeFrom = newPartnerAgeFrom;
		partnerAgeTo = newPartnerAgeTo;
	}

}
