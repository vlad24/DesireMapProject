package inputArchitecturePackage.actionQueryObjectPackage;

import actionCodeConstantsPackage.ActionCodesMaster;
import inputArchitecturePackage.Cryteria;
import desireContentPackage.Coordinates;

public class SatisfyPack extends ActionQueryObject{
	private final Coordinates coordinates;
	private final Cryteria cryteria;
	////
	public SatisfyPack(Coordinates newCoordinates, Cryteria newCryteria){
		super(ActionCodesMaster.ActionCodes.SatisfyCode);
		coordinates = newCoordinates;
		cryteria = newCryteria;
	}
	public Coordinates getCoordinates() {
		return coordinates;
	}
	public Cryteria getCryteria() {
		return cryteria;
	}
}