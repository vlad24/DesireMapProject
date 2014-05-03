package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.RegistrationPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToRegister;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.CommandsList;

public class HandlerRegister extends Handler{

	public HandlerRegister(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public boolean myJob(char action) {
		return (action == CodesMaster.ActionCodes.RegistrationCode);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, CommandsList accumulatorQueue) {
		if(myJob(qObject.actionCode)){
			System.out.println("Handler Register is pushed to queue");
			RegistrationPack registrationPack = (RegistrationPack) qObject;
			accumulatorQueue.addCommand((new CommandToRegister(ownerThread, registrationPack)));
			System.out.println("Registration command is pushed to queue");
		}
		
	}
}
