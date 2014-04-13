package desireMapApplicationPackage.desireContentPackage;


import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;


public final class DesireContentDating extends DesireContent{
	public final char partnerSex;
	public final int partnerAge;	
	////
	public DesireContentDating(String nLogin,int nDesireID, String nDesireDescription, Coordinates nCoordinates,
			char newPartnerSex, int newPartnerAge){
		super(nLogin , nDesireID, nDesireDescription, nCoordinates, CodesMaster.Categories.DatingCode);
		partnerSex = newPartnerSex;
		partnerAge = newPartnerAge; 		
	}

}
