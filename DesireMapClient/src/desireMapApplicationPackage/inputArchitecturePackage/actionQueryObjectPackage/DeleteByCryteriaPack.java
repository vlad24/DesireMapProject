package desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage;

import desireMapApplicationPackage.CodeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.inputArchitecturePackage.Cryteria;


public class DeleteByCryteriaPack extends DeletePack{
	
	public final Cryteria cryteria;
	public DeleteByCryteriaPack(Cryteria newCryteria) {
		super(CodesMaster.ActionCodes.DeleteByCryteriaCode);
		cryteria = newCryteria;
	}
}
