package desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage;

import desireMapApplicationPackage.CodeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.inputArchitecturePackage.Cryteria;


public class AddSatisfyPack extends ActionQueryObject{
	public final DesireContent content;
	public final Cryteria cryteria;
	////
	public AddSatisfyPack(DesireContent newContent, Cryteria newCryteria){
		super(CodesMaster.ActionCodes.AddSatisfyCode);
		content = newContent;
		cryteria = newCryteria;
	}
}
