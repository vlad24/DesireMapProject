package inputArchitecturePackage.actionQueryObjectPackage;

import actionCodeConstantsPackage.ActionCodesMaster;
import desireContentPackage.DesireContent;

public class DeleteByContentPack extends DeletePack{
	private final DesireContent content;
	protected DeleteByContentPack(DesireContent newContent) {
		super(ActionCodesMaster.ActionCodes.DeleteByContentCode);
		content = newContent;
	}
	public DesireContent getContent() {
		return content;
	}

}
