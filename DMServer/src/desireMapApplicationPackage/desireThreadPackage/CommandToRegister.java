package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.RegistrationPack;

public class CommandToRegister extends CommandForDesireThread{
	
	private final RegistrationPack regPack;
	//--
	public CommandToRegister(DesireThread newReceiver, RegistrationPack newRegPack) {
		receiver = newReceiver;
		regPack = newRegPack;
	}

	@Override
	public void execute() throws Exception {
		try{
			System.out.println("***Registering ");
			receiver.register(regPack);
			receiver.setCurrentUser(regPack.registrationData.login);
			receiver.setDeviceID(regPack.androidData.regID);
			System.out.println("***" + regPack.registrationData.login + " online");
			receiver.sendTrue();
		}
		catch(Exception error){
			receiver.sendFalse();
			throw error;
		}
	}

	@Override
	public void unexecute() {
		System.out.println("Unexecuting registering. No unexecution available.");
	}
	
}

