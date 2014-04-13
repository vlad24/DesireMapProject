package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.ShowPersonalDesiresPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToShowPersonalDesires;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;

public class HandlerShowPersonalDesires extends Handler{

	public HandlerShowPersonalDesires(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue) {
		if (myJob(qObject.actionCode)){
			ShowPersonalDesiresPack showPersonalDesiresPack = (ShowPersonalDesiresPack) qObject;
			accumulatorQueue.addCommand(new CommandToShowPersonalDesires(ownerThread, showPersonalDesiresPack.category));
		}
	}

	@Override
	public boolean myJob(char action) {
		return action == CodesMaster.ActionCodes.ShowPersonalDesiresCode;
	}

}
