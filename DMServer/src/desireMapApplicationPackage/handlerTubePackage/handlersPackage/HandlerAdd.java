package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToAddDesire;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.AddPack;


public class HandlerAdd extends Handler{

	public HandlerAdd(DesireThread inThread) {
		super(inThread);
	}

	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue){
		if (myJob(qObject.actionCode)){
			System.out.println("&& Casting to AddPack");
				AddPack addPack  = (AddPack) qObject;
				accumulatorQueue.addCommand(new CommandToAddDesire(ownerThread, addPack));
			}
		}

	public boolean myJob(char action){
		return CodesMaster.someAddCode(action);
	}
}