package desireMapApplicationPackage.actionQueryObjectPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.userDataPackage.AndroidData;
import desireMapApplicationPackage.userDataPackage.LoginData;

public class LoginPack extends ActionQueryObject{
	public final LoginData loginData;
	public final AndroidData androidData;
	public LoginPack(LoginData newLoginData, AndroidData newAndroidData) {
		super(CodesMaster.ActionCodes.LoginCode);
		loginData = newLoginData;
		androidData = newAndroidData;
	}
}
