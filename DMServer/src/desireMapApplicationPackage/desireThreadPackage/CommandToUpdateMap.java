package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.TilesPack;

public class CommandToUpdateMap extends CommandForDesireThread {

	public final TilesPack tilesPack;
	
	public CommandToUpdateMap(DesireThread newReceiver, TilesPack newTiles){
		receiver = newReceiver;
		tilesPack = newTiles;
	}

	@Override
	public void execute() throws Exception {
		receiver.updateSatisfiers(tilesPack);
	}

}
