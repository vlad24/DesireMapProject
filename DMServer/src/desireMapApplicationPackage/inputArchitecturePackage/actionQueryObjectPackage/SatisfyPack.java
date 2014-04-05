package desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage;

import java.util.HashSet;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.Coordinates;
import desireMapApplicationPackage.inputArchitecturePackage.Cryteria;

public class SatisfyPack extends ActionQueryObject{
	public String category;
	HashSet<String> tiles;
	public SatisfyPack(String newCategory, HashSet<String> newTiles){
		super(CodesMaster.ActionCodes.SatisfyCode);
		category = newCategory;
		tiles = newTiles;
	}
}