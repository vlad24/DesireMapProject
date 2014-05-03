package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.ShowPersonalDesiresPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToShowPersonalDesires;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.CommandsList;

public class HandlerShowPersonalDesires extends Handler{

	public HandlerShowPersonalDesires(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, CommandsList accumulatorQueue) {
		if (myJob(qObject.actionCode)){
			System.out.println("Handler Show Personal Desires is working");
			ShowPersonalDesiresPack showPersonalDesiresPack = (ShowPersonalDesiresPack) qObject;
			accumulatorQueue.addCommand(new CommandToShowPersonalDesires(ownerThread, showPersonalDesiresPack.category));
			System.out.println("Command to show personal desires is working");
		}
	}

	@Override
	public boolean myJob(char action) {
		return action == CodesMaster.ActionCodes.ShowPersonalDesiresCode;
	}

}
