package desireMapApplicationPackage.actionQueryObjectPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireContentPackage.DesireContent;

public class AddPack extends ActionQueryObject {
	public final DesireContent desireContent;
	public final String tile;
	public AddPack(DesireContent newContent, String newTile) {
		super(CodesMaster.ActionCodes.AddCode);
		desireContent = newContent;
		tile = newTile;
	}
	
}
