package desireMapApplicationPackage.actionQueryObjectPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.userDataPackage.RegistrationData;

public class RegistrationPack extends ActionQueryObject{
	public final RegistrationData registrationData;
	public RegistrationPack(RegistrationData newRegistrationData){
		super(CodesMaster.ActionCodes.RegistrationCode);
		registrationData = newRegistrationData;
	}
}