package desireMapApplicationPackage.desireThreadPackage;

import java.io.IOException;

import desireMapApplicationPackage.actionQueryObjectPackage.MessageDeliverPack;


public class CommandToGetMessages extends CommandForDesireThread{
	public MessageDeliverPack deliverPack;
	//--
	public CommandToGetMessages(DesireThread newReceiver, MessageDeliverPack newPack){
		receiver = newReceiver;
		deliverPack = newPack;
	}
	@Override
	public void execute() throws Exception{
		if (deliverPack.hoursRadius == 0){
			try{
				receiver.loadNewMessages();
			}
			catch(Exception error){
				error.printStackTrace();
				throw error;
			}
		}
		else{
			try {
				receiver.getOldMessagesByCryteria(deliverPack);
				receiver.socketOut.reset();
			} catch (Exception error) {
				error.printStackTrace();
				throw error;
			}
		}
	}

}
