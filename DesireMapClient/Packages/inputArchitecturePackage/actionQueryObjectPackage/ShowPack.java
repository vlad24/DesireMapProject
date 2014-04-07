package inputArchitecturePackage.actionQueryObjectPackage;

import actionCodeConstantsPackage.ActionCodesMaster;
import inputArchitecturePackage.Cryteria;
import desireContentPackage.DesireContent;

public class ShowPack extends ActionQueryObject{
	private final Cryteria cryteria;
	////
	public ShowPack(DesireContent newContent, Cryteria newCryteria){
		super(ActionCodesMaster.ActionCodes.ShowPersonalDesiresCode);
		cryteria = newCryteria;
	}
	public Cryteria getCryteria() {
		return cryteria;
	}
}
