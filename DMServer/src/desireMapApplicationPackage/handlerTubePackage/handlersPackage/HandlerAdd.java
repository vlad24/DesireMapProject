package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToAddDesire;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.CommandsList;


public class HandlerAdd extends Handler{

	public HandlerAdd(DesireThread inThread) {
		super(inThread);
	}

	public void tryToHandleClientQuery(ActionQueryObject qObject, CommandsList accumulatorQueue){
		if (myJob(qObject.actionCode)){
			System.out.println("Handler adding is working");
			AddPack addPack  = (AddPack) qObject;
			accumulatorQueue.addCommand(new CommandToAddDesire(ownerThread, addPack));
			System.out.println("Add command is pushed to queue");
		}
	}

	public boolean myJob(char action){
		return (action == CodesMaster.ActionCodes.AddCode);
	}
}