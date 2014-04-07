package inputArchitecturePackage.actionQueryObjectPackage;

import actionCodeConstantsPackage.ActionCodesMaster;
import inputArchitecturePackage.Cryteria;
import desireContentPackage.DesireContent;

public class AddSatisfyPack extends ActionQueryObject{
	private final DesireContent content;
	private final Cryteria cryteria;
	////
	public AddSatisfyPack(DesireContent newContent, Cryteria newCryteria){
		super(ActionCodesMaster.ActionCodes.AddSatisfyCode);
		content = newContent;
		cryteria = newCryteria;
	}
}
