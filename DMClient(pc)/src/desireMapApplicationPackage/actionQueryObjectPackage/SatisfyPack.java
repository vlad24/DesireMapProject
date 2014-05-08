package desireMapApplicationPackage.actionQueryObjectPackage;

import java.util.HashSet;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;

public class SatisfyPack extends ActionQueryObject{
	public String sDesireID;
	public Integer sCategoryCode;
	public HashSet<String> tiles;
	public int tileDepth;
	
	public SatisfyPack(String newDesireID, HashSet<String> newTiles, int newTileDepth){
		super(CodesMaster.ActionCodes.SatisfyCode);
		sCategoryCode = null;
		sDesireID = newDesireID;
		tiles = newTiles;
		tileDepth = newTileDepth;
	}
	
	public SatisfyPack(int newCategory, HashSet<String> newTiles, int newTileDepth){
		super(CodesMaster.ActionCodes.SatisfyCode);
		sCategoryCode = newCategory;
		sDesireID = null;
		tiles = newTiles;
		tileDepth = newTileDepth;
	}
}