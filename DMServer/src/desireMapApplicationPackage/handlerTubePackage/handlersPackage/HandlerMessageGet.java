package desireMapApplicationPackage.handlerTubePackage.handlersPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.actionQueryObjectPackage.MessageDeliverPack;
import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.desireThreadPackage.CommandToGetMessages;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.CommandsList;

public class HandlerMessageGet extends Handler{
	public HandlerMessageGet(DesireThread surroundingThread) {
		super(surroundingThread);
	}

	@Override
	public void tryToHandleClientQuery(ActionQueryObject qObject, CommandsList accumulatorQueue) {
		if (myJob(qObject.actionCode)){
			System.out.println("HandlerMessageGet is working");
			if (qObject.actionCode == CodesMaster.ActionCodes.MessageDeliverCode){
				System.out.println("Some old messages will be delivered");
				MessageDeliverPack deliverPack = (MessageDeliverPack) qObject;  
				accumulatorQueue.addCommand(new CommandToGetMessages(ownerThread, deliverPack));
				System.out.println("'Old message deliver' command is pushed to queue");
			}
			else{
				System.out.println("New messages will be delivered");
				accumulatorQueue.addCommand(new CommandToGetMessages(ownerThread, new MessageDeliverPack(null, null, CodesMaster.SpecialValues.radiusForAllNew)));
				System.out.println("'New message deliver' command is pushed to queue");
			}
		}
	}

	@Override
	public boolean myJob(char action) {
		return ((action == CodesMaster.ActionCodes.MessageDeliverCode) || (action == CodesMaster.ActionCodes.LoginCode)); 
	}
}
