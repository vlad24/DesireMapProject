package desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.DesireContent;

public class AddPack extends ActionQueryObject {
	public final DesireContent desireContent;
	public AddPack(DesireContent newContent) {
		super(CodesMaster.ActionCodes.AddCode);
		desireContent = newContent;
	}
	
}
