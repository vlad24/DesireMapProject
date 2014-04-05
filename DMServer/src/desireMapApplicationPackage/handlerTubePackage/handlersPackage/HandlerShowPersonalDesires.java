package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToShowPersonalDesires;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.ShowPersonalDesiresPack;

public class HandlerShowPersonalDesires extends Handler{

	public HandlerShowPersonalDesires(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue) {
		if (myJob(qObject.actionCode)){
			ShowPersonalDesiresPack showPersonalDesiresPack = (ShowPersonalDesiresPack) qObject;
			accumulatorQueue.addCommand(new CommandToShowPersonalDesires(ownerThread, showPersonalDesiresPack.cryteria));
		}
	}

	@Override
	public boolean myJob(char action) {
		return action == CodesMaster.ActionCodes.ShowPersonalDesiresCode;
	}

}
