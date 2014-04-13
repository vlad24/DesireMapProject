package desireMapApplicationPackage.handlerTubePackage.handlersPackage;


import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.LoginPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToLogIn;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;

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
