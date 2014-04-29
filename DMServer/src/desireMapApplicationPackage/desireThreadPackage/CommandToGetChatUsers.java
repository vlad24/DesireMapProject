package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.outputSetPackage.UserSet;


public class CommandToGetChatUsers extends CommandForDesireThread {

	public CommandToGetChatUsers(DesireThread newReceiver){
		receiver = newReceiver;
	}
	
	@Override
	public void execute() throws Exception {
		try{
			System.out.println("CommandToGetChatUsers executed");
			UserSet userSet = receiver.getUsersTalkedTo();
			receiver.socketOut.writeObject(userSet);
			receiver.socketOut.flush();
			receiver.socketOut.reset();
		}
		catch(Exception error){
			System.out.println("-Error at execution of CommandToGetChatUsers");
			throw error;
		}
	}

}
