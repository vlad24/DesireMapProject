package desireMapApplicationPackage.handlerTubePackage.handlersPackage;


import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.LoginPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToLogIn;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.CommandsList;

public class HandlerLogIn extends Handler{

	public HandlerLogIn(DesireThread inThread) {
		super(inThread);
	}

	@Override
	public boolean myJob(char action) {
		return (action == CodesMaster.ActionCodes.LoginCode);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, CommandsList accumulatorQueue){
		if(myJob(qObject.actionCode)){
			System.out.println("Handler Log In is working");
			LoginPack loginPack = (LoginPack)qObject;
			accumulatorQueue.addCommand(new CommandToLogIn(ownerThread, loginPack));
			System.out.println("Log In command is pushed to queue");
		}
	}
}
