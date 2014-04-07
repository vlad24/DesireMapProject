package inputArchitecturePackage.actionQueryObjectPackage;

import actionCodeConstantsPackage.ActionCodesMaster;
import userDataPackage.LoginData;

public class LoginPack extends ActionQueryObject{
	private final LoginData loginData;
	protected LoginPack(char newActionCode, LoginData newLoginData) {
		super(ActionCodesMaster.ActionCodes.LoginCode);
		loginData = newLoginData;
	}
	public LoginData getLoginData() {
		return loginData;
	}
}
