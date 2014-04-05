package desireMapApplicationPackage.handlerTubePackage.handlersPackage;


import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToLogIn;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.LoginPack;

public class HandlerLogIn extends Handler{

	public HandlerLogIn(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public boolean myJob(char action) {
		return (action == CodesMaster.ActionCodes.LoginCode);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue){
		if(myJob(qObject.actionCode)){
			LoginPack loginPack = (LoginPack)qObject;
			accumulatorQueue.addCommand(new CommandToLogIn(ownerThread, loginPack.loginData));
		}
	}
}
