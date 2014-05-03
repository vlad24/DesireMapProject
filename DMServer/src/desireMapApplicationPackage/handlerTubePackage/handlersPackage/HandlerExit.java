package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToExit;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.CommandsList;


public class HandlerExit extends Handler{

	public HandlerExit(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, CommandsList accumulatorQueue) {
		if(myJob(qObject.actionCode)){
			System.out.println("Handler exit is working");
			accumulatorQueue.addCommand(new CommandToExit(ownerThread));
			System.out.println("Exit command is pushed to queue");
		}
	}
	
	public boolean myJob(char action){
		return (action == CodesMaster.ActionCodes.ExitCode);
	}
}
