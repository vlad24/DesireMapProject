package desireMapApplicationPackage.actionQueryObjectPackage;

import java.util.HashSet;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;

public class SatisfyPack extends ActionQueryObject{
	public String sDesireID;
	public HashSet<String> tiles;
	public int tileDepth;
	public SatisfyPack(String newDesireID, HashSet<String> newTiles, int newTileDepth){
		super(CodesMaster.ActionCodes.SatisfyCode);
		sDesireID = newDesireID;
		tiles = newTiles;
		tileDepth = newTileDepth;
	}
}