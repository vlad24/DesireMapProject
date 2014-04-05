package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.ActionQueryObject;


public abstract class Handler {
	
	protected DesireThread ownerThread;
	//--
	public Handler(DesireThread surroundingThread){
		ownerThread = surroundingThread;
	}
	
	public abstract void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue);
	public abstract boolean myJob(char action);
}