package desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.inputArchitecturePackage.Cryteria;

public class SatisfyPack extends ActionQueryObject{
	public final Coordinates coordinates;
	public final Cryteria cryteria;
	public SatisfyPack(Coordinates newCoordinates, Cryteria newCryteria){
		super(CodesMaster.ActionCodes.SatisfyCode);
		coordinates = newCoordinates;
		cryteria = newCryteria;
	}
}