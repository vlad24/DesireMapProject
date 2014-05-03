package desireMapApplicationPackage.desireThreadPackage;

import java.io.IOException;

import desireMapApplicationPackage.actionQueryObjectPackage.MessageDeliverPack;
import desireMapApplicationPackage.outputSetPackage.MessageSet;


public class CommandToGetMessages extends CommandForDesireThread{
	public MessageDeliverPack deliverPack;
	//--
	public CommandToGetMessages(DesireThread newReceiver, MessageDeliverPack newPack){
		receiver = newReceiver;
		deliverPack = newPack;
	}
	@Override
	public void execute() throws Exception{
		try{
			if (deliverPack.hoursRadius == 0){
				receiver.loadNewMessages();
			}
			else{
				MessageSet mSet = receiver.getOldMessagesByCryteria(deliverPack);
				receiver.socketOut.writeObject(mSet);
				receiver.socketOut.reset();
			}
		}
		catch(Exception error){
			receiver.socketOut.writeObject(null);
			receiver.socketOut.reset();
			throw error;
		}
	}

}
