package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToShowInfo;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;


public class HandlerShowInfo extends Handler{

	public HandlerShowInfo(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue){
		if (myJob(qObject.actionCode)){
			accumulatorQueue.addCommand((new CommandToShowInfo(ownerThread)));
		}
	}

	@Override
	public boolean myJob(char action) {
		return action == CodesMaster.ActionCodes.ShowInfoCode;
	}
}