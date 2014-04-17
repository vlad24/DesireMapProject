package desireMapApplicationPackage.actionQueryObjectPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.userDataPackage.LoginData;

public class LoginPack extends ActionQueryObject{
	public final LoginData loginData;
	public LoginPack(LoginData newLoginData) {
		super(CodesMaster.ActionCodes.LoginCode);
		loginData = newLoginData;
	}
}
