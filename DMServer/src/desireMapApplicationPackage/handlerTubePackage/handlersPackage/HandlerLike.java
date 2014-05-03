package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.LikePack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToLike;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.CommandsList;

public class HandlerLike extends Handler {

	public HandlerLike(DesireThread surroundingThread) {
		super(surroundingThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, CommandsList accumulatorQueue) {
		if (myJob(qObject.actionCode)){
			LikePack likePack = (LikePack) qObject;
			accumulatorQueue.addCommand(new CommandToLike(this.ownerThread, likePack));
		}
	}

	@Override
	public boolean myJob(char action) {
		return (action == CodesMaster.ActionCodes.LikeCode);
	}

}
