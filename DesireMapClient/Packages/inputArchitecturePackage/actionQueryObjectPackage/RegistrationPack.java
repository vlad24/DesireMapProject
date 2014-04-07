package inputArchitecturePackage.actionQueryObjectPackage;

import actionCodeConstantsPackage.ActionCodesMaster;
import userDataPackage.RegistrationData;

public class RegistrationPack extends ActionQueryObject{
	private final RegistrationData registrationData;
	
	RegistrationPack(RegistrationData newRegistrationData){
		super(ActionCodesMaster.ActionCodes.RegistrationCode);
		registrationData = newRegistrationData;
	}

	public RegistrationData getRegistrationData() {
		return registrationData;
	}
	
}
