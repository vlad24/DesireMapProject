package desireMapApplicationPackage.actionQueryObjectPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;

public class ShowPersonalDesiresPack extends ActionQueryObject{
	public int category;
	public ShowPersonalDesiresPack(int newCategory){
		super(CodesMaster.ActionCodes.ShowPersonalDesiresCode);
		category = newCategory;
	}
}
