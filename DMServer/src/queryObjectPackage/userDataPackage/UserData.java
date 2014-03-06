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
	protected UserData(char nTypeCode, String newLogin, int newUserId) {
		super(nTypeCode);
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
