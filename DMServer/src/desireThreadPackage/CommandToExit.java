package desireThreadPackage;


public class CommandToExit extends CommandForDesireThread{

	public CommandToExit(DesireThread newReceiver){
		receiver = newReceiver;
	}
	@Override
	public void execute() throws Exception {
		try{
			System.out.println("*** " + receiver.getCurrentUser() + " exiting");
			receiver.setCurrentUser("?");
			receiver.instrument.exit();
			Thread.currentThread().interrupt();
			receiver.confirmSuccess();
			if (Thread.currentThread().isInterrupted())
				System.out.println("*** THREAD HAS BEEN INTERRUPTED");
			//Cleaning the data base is going to appear here, it can generate exceptions
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}

	}

}
