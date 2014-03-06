/*
 * 			QueryObject
 * 		   /		   \
 * 		UserData	DesirePack
 */

package queryObjectPackage.userDataPackage;

import queryObjectPackage.QueryObject;

public abstract class UserData extends QueryObject{
	protected final String login;
	protected final int userId;
	////
	protected UserData(char nActionCode, char nTypeCode, String newLogin, int newUserId) {
		super(nActionCode, nTypeCode);
		login = newLogin;
		userId = newUserId;
	}
	public String getLogin(){
		return login;
	}
	public int getUserId(){
		return userId;
	}
}
