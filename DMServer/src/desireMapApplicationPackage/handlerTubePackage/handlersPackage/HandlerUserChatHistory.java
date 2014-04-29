package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.UserChatHistoryPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToGetChatUsers;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;

public class HandlerUserChatHistory extends Handler{

	public HandlerUserChatHistory(DesireThread surroundingThread) {
		super(surroundingThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, QueueOfCommands accumulatorQueue) {
		if (myJob(qObject.actionCode)){
			System.out.println("Handler UserChatHistory is working");
			UserChatHistoryPack chatHistoryPack = (UserChatHistoryPack) qObject;
			accumulatorQueue.addCommand(new CommandToGetChatUsers(ownerThread));
			System.out.println("Command to get chat users pushed to the queue");
		}
	}

	@Override
	public boolean myJob(char action) {
		return action == CodesMaster.ActionCodes.UserHistoryCode;
	}

}
