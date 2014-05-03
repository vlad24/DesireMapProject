package desireMapApplicationPackage.actionQueryObjectPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;

public class LikePack extends ActionQueryObject{
	public final String desireID;
	public final boolean isLiked;
	public LikePack(String newDesireID, boolean newIsLiked){
		super(CodesMaster.ActionCodes.LikeCode);
		desireID = newDesireID;
		isLiked = newIsLiked;
	}
}
