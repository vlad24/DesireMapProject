package inputArchitecturePackage.actionQueryObjectPackage;

import actionCodeConstantsPackage.ActionCodesMaster;
import inputArchitecturePackage.Cryteria;

public class DeleteByCryteriaPack extends DeletePack{
	
	private final Cryteria cryteria;
	protected DeleteByCryteriaPack(Cryteria newCryteria) {
		super(ActionCodesMaster.ActionCodes.DeleteByCryteriaCode);
		cryteria = newCryteria;
	}
	public Cryteria getCryteria() {
		return cryteria;
	}
}
