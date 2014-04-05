package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToGetSatisfiers;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.SatisfyPack;

public class HandlerSatisfy extends Handler{

	public HandlerSatisfy(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue) {
		if (myJob(qObject.actionCode)){
			SatisfyPack satisfyPack = (SatisfyPack) qObject;
			accumulatorQueue.addCommand(new CommandToGetSatisfiers(ownerThread, satisfyPack));
		}
	}

	@Override
	public boolean myJob(char action) {
		return CodesMaster.someSatisfyCode(action);
	}
}