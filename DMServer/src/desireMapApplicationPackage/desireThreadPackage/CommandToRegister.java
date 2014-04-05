package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.userDataPackage.RegistrationData;

public class CommandToRegister extends CommandForDesireThread{
	
	private final RegistrationData regData;
	//--
	public CommandToRegister(DesireThread newReceiver, RegistrationData newRegData) {
		receiver = newReceiver;
		regData = newRegData;
	}

	@Override
	public void execute() throws Exception {
		try{
			System.out.println("***Registering ");
			synchronized(this){
				receiver.register(regData);
			}
			receiver.setCurrentUser(regData.login);
			System.out.println("***" + regData.login + " online");
			receiver.registerTalker(false);
			receiver.confirmSuccess();
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}
	}
	
}

