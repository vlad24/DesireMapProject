package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.RegistrationPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToRegister;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;

public class HandlerRegister extends Handler{

	public HandlerRegister(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public boolean myJob(char action) {
		return (action == CodesMaster.ActionCodes.RegistrationCode);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue) {
		if(myJob(qObject.actionCode)){
			RegistrationPack registrationPack = (RegistrationPack) qObject;
			accumulatorQueue.addCommand((new CommandToRegister(ownerThread, registrationPack.registrationData)));
		}
		
	}
}
