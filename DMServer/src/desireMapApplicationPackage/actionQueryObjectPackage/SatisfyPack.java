package desireMapApplicationPackage.actionQueryObjectPackage;

import java.util.HashSet;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;

public class SatisfyPack extends ActionQueryObject{
	public int sDesireID;
	public HashSet<String> tiles;
	public SatisfyPack(int newDesireID, HashSet<String> newTiles){
		super(CodesMaster.ActionCodes.SatisfyCode);
		sDesireID = newDesireID;
		tiles = newTiles;
	}
}