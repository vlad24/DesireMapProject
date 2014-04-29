package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.messageSystemPackage.*;

public class CommandToSendMessage extends CommandForDesireThread{
	private ClientMessage clientMessage;
	////
	public CommandToSendMessage(DesireThread newReceiver, ClientMessage newMessage){
		receiver = newReceiver;
		clientMessage = newMessage;
	}
	
	@Override
	public void execute() throws Exception {
		receiver.postMessage(clientMessage);
	}

}
