package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.LoginPack;

public class CommandToLogIn extends CommandForDesireThread{
	
	private final LoginPack logPack;
	//--
	public CommandToLogIn(DesireThread newReceiver, LoginPack newLoginPack) {
		receiver = newReceiver;
		logPack = newLoginPack;
	}
	@Override
	public void execute() throws Exception {
		try{
			System.out.println("*** Logging In executed");
			receiver.authorize(logPack);
			receiver.setCurrentUser(logPack.loginData.login);
			receiver.setDeviceID(logPack.androidData.regID);
			receiver.sendTrue();
			System.out.println("*** " + logPack.loginData.login + " ONLINE");
		}
		catch(Exception error){
			receiver.sendFalse();
			throw error;
		}
	}
	
}
