package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.userDataPackage.LoginData;

public class CommandToLogIn extends CommandForDesireThread{
	
	private final LoginData logData;
	//--
	public CommandToLogIn(DesireThread newReceiver, LoginData newLoginData) {
		receiver = newReceiver;
		logData = newLoginData;
	}
	@Override
	public void execute() throws Exception {
		try{
			System.out.println("*** Logging IN");
			synchronized(receiver){
				receiver.logIn(logData);
			}
			receiver.confirmSuccess();
			receiver.setCurrentUser(logData.login);
			receiver.registerTalker(true);
			System.out.println("*** " + logData.login + " ONLINE");
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}
