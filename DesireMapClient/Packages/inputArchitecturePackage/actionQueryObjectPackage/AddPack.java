package inputArchitecturePackage.actionQueryObjectPackage;

import desireContentPackage.DesireContent;

public class AddPack extends ActionQueryObject {
	private final DesireContent desireContent;
	protected AddPack(char nActionCode, DesireContent newContent) {
		super(nActionCode);
		desireContent = newContent;
	}
	
}
