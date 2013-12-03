
public class CommandToExit extends CommandForDesireThread{

	public CommandToExit(DesireThread newReceiver){
		receiver = newReceiver;
	}
	@Override
	public void execute() throws Exception {
		try{
			System.out.println("*** " + receiver.currentUser + " exiting");
			receiver.currentUser = "?";
			receiver.instrument.exit();
			receiver.interrupt();
			receiver.confirmSuccess();
			if (receiver.isInterrupted())
				System.out.println("*** THREAD HAS BEEN INTERRUPTED");
			//Cleaning the data base is going to appear here, it can generate exceptions
		}
		catch(Exception error){
			receiver.confirmFail();
			throw error;
		}

	}

}
