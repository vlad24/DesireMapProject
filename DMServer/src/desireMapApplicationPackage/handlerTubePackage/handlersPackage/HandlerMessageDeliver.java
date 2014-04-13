package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToDeliverMessages;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;

public class HandlerMessageDeliver extends Handler{
	public HandlerMessageDeliver(DesireThread surroundingThread) {
		super(surroundingThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue) {
		if (myJob(qObject.actionCode)){
			System.out.println("HandlerMessageDeliver is working");
			accumulatorQueue.addCommand(new CommandToDeliverMessages(ownerThread));
		}
	}

	@Override
	public boolean myJob(char action) {
		return (action == CodesMaster.ActionCodes.MessageDeliverCode); 
	}
}
