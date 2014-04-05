package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToSendMessage;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage.MessageSendPack;

public class HandlerMessageSend extends Handler{

	public HandlerMessageSend(DesireThread surroundingThread) {
		super(surroundingThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue) {
		if (myJob(qObject.actionCode)){
			System.out.println("HandlerMessageSend is working");
			MessageSendPack mesPack = (MessageSendPack) qObject;
			accumulatorQueue.addCommand(new CommandToSendMessage(ownerThread, mesPack.message));
		}
	}

	@Override
	public boolean myJob(char action) {
		return (action == CodesMaster.ActionCodes.MessageSendCode); 
	}

}
