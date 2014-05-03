package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.DeletePack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToDelete;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.CommandsList;

public class HandlerDelete extends Handler{

	public HandlerDelete(DesireThread surroundingThread) {
		super(surroundingThread);
	}
	
	
	public boolean myJob(char action){
		return (action == CodesMaster.ActionCodes.DeleteCode);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, CommandsList accumulatorQueue) {
		if (myJob(qObject.actionCode)){
			System.out.println("Handler delete is working");
			DeletePack deletePack = (DeletePack) qObject;
			accumulatorQueue.addCommand(new CommandToDelete(ownerThread, deletePack));
			System.out.println("Delete command is pushed to queue");
		}
	}

}
