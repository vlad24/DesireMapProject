package desireMapApplicationPackage.desireThreadPackage;

public class CommandToExit extends CommandForDesireThread{

	public CommandToExit(DesireThread newReceiver){
		receiver = newReceiver;
	}
	@Override
	public void execute() throws Exception {
		try{
			System.out.println("*** " + receiver.getUserName() + " exiting");
			receiver.exit();
			receiver.setCurrentUser("?");
			receiver.setDeviceID("-");
			Thread.currentThread().interrupt();
			receiver.sendTrue();
			if (Thread.currentThread().isInterrupted()){
				System.out.println("*** THREAD HAS BEEN INTERRUPTED");
				System.out.println("*** EXITING FINISHED");
			}
		}
		catch(Exception error){
			receiver.sendFalse();
			throw error;
		}

	}

}
