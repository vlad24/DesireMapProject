package queryObjectPackage.desirePackPackage.desireCategoriesPackage;

import java.io.Serializable;

import queryObjectPackage.desirePackPackage.DesirePack;


public final class DesirePackDating extends DesirePack{
	private final char partnerSex;
	private final int partnerAge;	
	////
	public DesirePackDating(char nTypeCode, String nLogin, String nDesireDescription, double nLatitude, double nLongitude, char newPartnerSex, int newPartnerAge){
		super('d', nLogin, nDesireDescription, nLatitude, nLongitude);
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
