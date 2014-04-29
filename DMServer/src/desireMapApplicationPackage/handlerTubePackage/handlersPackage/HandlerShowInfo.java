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
			System.out.println("Handler Log In is working");
			accumulatorQueue.addCommand((new CommandToShowInfo(ownerThread)));
			System.out.println("Show info command is pushed to queue");
		}
	}

	@Override
	public boolean myJob(char action) {
		return action == CodesMaster.ActionCodes.ShowInfoCode;
	}
}