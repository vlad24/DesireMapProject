package desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage;

import desireMapApplicationPackage.CodeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.inputArchitecturePackage.Cryteria;


public class ShowPersonalDesiresPack extends ActionQueryObject{
	public final Cryteria cryteria;
	////
	public ShowPersonalDesiresPack(Cryteria newCryteria){
		super(CodesMaster.ActionCodes.ShowPersonalDesiresCode);
		cryteria = newCryteria;
	}
}
