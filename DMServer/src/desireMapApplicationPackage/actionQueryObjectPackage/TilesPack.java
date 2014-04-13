package desireMapApplicationPackage.actionQueryObjectPackage;

import java.util.HashSet;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;

public class TilesPack extends ActionQueryObject{
	public int depth;
	public HashSet<String> tiles;
	public TilesPack(int newLength, HashSet<String> newTiles){
		super(CodesMaster.ActionCodes.TilesCode);
		depth = newLength;
		tiles = newTiles;
	}
}
