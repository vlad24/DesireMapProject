package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToGetSatisfiers;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;

public class HandlerSatisfy extends Handler{

	public HandlerSatisfy(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue) {
		if (myJob(qObject.actionCode)){
			System.out.println("Handler Satisfy is working");
			SatisfyPack satisfyPack = (SatisfyPack) qObject;
			accumulatorQueue.addCommand(new CommandToGetSatisfiers(ownerThread, satisfyPack));
			System.out.println("Satisfy command is pushed to queue");
		}
	}

	@Override
	public boolean myJob(char action) {
		return action == CodesMaster.ActionCodes.SatisfyCode;
	}
}