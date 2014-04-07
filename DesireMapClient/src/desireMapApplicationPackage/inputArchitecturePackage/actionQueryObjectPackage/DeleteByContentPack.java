package desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage;

import desireMapApplicationPackage.CodeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.DesireContent;


public class DeleteByContentPack extends DeletePack{
	public final DesireContent content;
	public DeleteByContentPack(DesireContent newContent) {
		super(CodesMaster.ActionCodes.DeleteByContentCode);
		content = newContent;
	}

}
