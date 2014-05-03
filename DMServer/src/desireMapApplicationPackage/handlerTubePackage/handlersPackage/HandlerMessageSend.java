package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.MessageSendPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToSendMessage;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.CommandsList;

public class HandlerMessageSend extends Handler{

	public HandlerMessageSend(DesireThread surroundingThread) {
		super(surroundingThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, CommandsList accumulatorQueue) {
		if (myJob(qObject.actionCode)){
			System.out.println("HandlerMessageSend is working");
			MessageSendPack mesPack = (MessageSendPack) qObject;
			accumulatorQueue.addCommand(new CommandToSendMessage(ownerThread, mesPack.clientMessage));
			System.out.println("Message send command is pushed to queue");
		}
	}

	@Override
	public boolean myJob(char action) {
		return (action == CodesMaster.ActionCodes.MessageSendCode); 
	}

}
