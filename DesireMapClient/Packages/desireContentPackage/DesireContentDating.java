package desireContentPackage;

import actionCodeConstantsPackage.ActionCodesMaster;


public final class DesireContentDating extends DesireContent{
	private final char partnerSex;
	private final int partnerAge;	
	////
	public DesireContentDating(String nLogin,int nDesireID, String nDesireDescription, Coordinates nCoordinates,
			char newPartnerSex, int newPartnerAge){
		super(nLogin , nDesireID, nDesireDescription, nCoordinates, ActionCodesMaster.Categories.Dating);
		partnerSex = newPartnerSex;
		partnerAge = newPartnerAge; 		
	}

	public char getPartnerSex() {
		return partnerSex;
	}

	public int getPartnerAge() {
		return partnerAge;
	}
}
