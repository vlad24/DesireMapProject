package desireMapApplicationPackage.actionQueryObjectPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.userDataPackage.AndroidData;
import desireMapApplicationPackage.userDataPackage.RegistrationData;

public class RegistrationPack extends ActionQueryObject{
	public final RegistrationData registrationData;
	public final AndroidData androidData;
	public RegistrationPack(RegistrationData newRegistrationData, AndroidData newAndroidData){
		super(CodesMaster.ActionCodes.RegistrationCode);
		registrationData = newRegistrationData;
		androidData = newAndroidData;
	}
}