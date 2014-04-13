package desireMapApplicationPackage.desireThreadPackage;




public class CommandToExit extends CommandForDesireThread{

	public CommandToExit(DesireThread newReceiver){
		receiver = newReceiver;
	}
	@Override
	public void execute() throws Exception {
		try{
			System.out.println("*** " + receiver.getUserName() + " exiting");
			
			receiver.unregisterTalker();
			receiver.setCurrentUser("?");
			receiver.exit();
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
