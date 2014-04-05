package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.messageSystemPackage.*;

public class CommandToSendMessage extends CommandForDesireThread{
	private Message message;
	////
	public CommandToSendMessage(DesireThread newReceiver, Message newMessage){
		receiver = newReceiver;
		message = newMessage;
	}
	
	@Override
	public void execute() throws Exception {
		receiver.postMessage(message);
	}

}
