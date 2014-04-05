package desireMapApplicationPackage.desireThreadPackage;

import java.io.IOException;


public class CommandToDeliverMessages extends CommandForDesireThread{
	//--
	public CommandToDeliverMessages(DesireThread newReceiver){
		receiver = newReceiver;
	}
	@Override
	public void execute() throws Exception{
		try {
			receiver.sendDeliveredMessagesToClient();
			receiver.clearLocalMessageHistory();
			receiver.socketOut.reset();
		} catch (IOException error) {
			error.printStackTrace();
			throw error;
		}
	}

}
