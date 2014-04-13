package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.TilesPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToUpdateMap;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;

public class HandlerTiles extends Handler{

	public HandlerTiles(DesireThread surroundingThread) {
		super(surroundingThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue){
		if (myJob(qObject.actionCode)){
			TilesPack tilesPack = (TilesPack) qObject;
			accumulatorQueue.addCommand((new CommandToUpdateMap(ownerThread, tilesPack)));
		}
	}

	@Override
	public boolean myJob(char action) {
		return action == CodesMaster.ActionCodes.TilesCode;
	}
}
