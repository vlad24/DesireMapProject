package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToExit;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;


public class HandlerExit extends Handler{

	public HandlerExit(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue) {
		if(myJob(qObject.actionCode)){
			accumulatorQueue.addCommand(new CommandToExit(ownerThread));
		}
	}
	
	public boolean myJob(char action){
		return (action == CodesMaster.ActionCodes.ExitCode);
	}
}
