package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToDelete;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.DeletePack;

public class HandlerDelete extends Handler{

	public HandlerDelete(DesireThread surroundingThread) {
		super(surroundingThread);
	}
	
	
	public boolean myJob(char action){
		return (action == CodesMaster.ActionCodes.DeleteCode);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue) {
		if (myJob(qObject.actionCode)){
			DeletePack deletePack = (DeletePack) qObject;
			accumulatorQueue.addCommand(new CommandToDelete(ownerThread, deletePack));
		}
	}

}
